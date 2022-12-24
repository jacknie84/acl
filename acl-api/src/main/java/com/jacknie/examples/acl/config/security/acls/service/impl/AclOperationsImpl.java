package com.jacknie.examples.acl.config.security.acls.service.impl;

import com.jacknie.examples.acl.config.security.acls.domain.AclSource;
import com.jacknie.examples.acl.config.security.acls.domain.CustomAce;
import com.jacknie.examples.acl.config.security.acls.domain.SidModel;
import com.jacknie.examples.acl.config.security.acls.service.AclDomainIdConverterRepository;
import com.jacknie.examples.acl.config.security.acls.service.AclOperations;
import com.jacknie.examples.acl.config.security.acls.service.OidFactory;
import com.jacknie.examples.acl.jpa.acl.domain.AclDomain;
import com.jacknie.examples.acl.jpa.acl.domain.AclDomainRepository;
import com.jacknie.examples.acl.jpa.acl.entry.AclEntry;
import com.jacknie.examples.acl.jpa.acl.entry.AclEntryRepository;
import com.jacknie.examples.acl.jpa.acl.entry.AclSourceAcePart;
import com.jacknie.examples.acl.jpa.acl.oid.AclOid;
import com.jacknie.examples.acl.jpa.acl.oid.AclOidEntriesInheritingType;
import com.jacknie.examples.acl.jpa.acl.oid.AclOidRepository;
import com.jacknie.examples.acl.jpa.acl.oid.AclSourceBasePart;
import com.jacknie.examples.acl.jpa.acl.sid.AclSid;
import com.jacknie.examples.acl.jpa.acl.sid.AclSidKey;
import com.jacknie.examples.acl.jpa.acl.sid.AclSidRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.lang.Nullable;
import org.springframework.security.acls.model.*;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Builder
public class AclOperationsImpl implements AclOperations {

    private final AclOidRepository oidRepository;
    private final AclSidRepository sidRepository;
    private final AclDomainRepository domainRepository;
    private final AclEntryRepository entryRepository;
    private final AclDomainIdConverterRepository idConverterRepository;
    private final OidFactory oidFactory;

    @Override
    public boolean existsObjectIdentity(ObjectIdentity oid) {
        Assert.hasText(oid.getType(), "ObjectIdentity type cannot be blank");
        Assert.notNull(oid.getIdentifier(), "ObjectIdentity identifier cannot be null");
        return oidRepository.exists(oid);
    }

    @Override
    public void createObjectIdentity(ObjectIdentity oid, Sid owner, @Nullable Serializable parentId) {
        Assert.notNull(oid, "oid cannot be null");
        Assert.notNull(owner, "owner cannot be null");
        AclSid aclSid = getOrCreateAclSid(owner);
        Class<? extends Serializable> idType = oid.getIdentifier().getClass();
        String idConverterName = idConverterRepository.findName(idType)
            .orElseThrow(() -> new IllegalStateException("cannot found id converter by id type: " + idType));
        String domainCode = oid.getType();
        AclDomain domain = domainRepository.findByCode(domainCode).orElseGet(() -> saveAclDomain(domainCode, idConverterName));
        AclOid parent = parentId != null
            ? oidRepository.findById((Long) parentId).orElseThrow(() -> new IllegalArgumentException("cannot found AclObject entity by id: " + parentId))
            : null;
        AclOid entity = AclOid.builder()
            .identifier(oid.getIdentifier().toString())
            .entriesInheritingType(AclOidEntriesInheritingType.SOFT)
            .ownerSid(aclSid)
            .domain(domain)
            .parent(parent)
            .build();
        oidRepository.save(entity);
    }

    @Override
    public void deleteAcl(ObjectIdentity oid, boolean deleteChildren) {
        Assert.notNull(oid, "Object Identity required");
        Assert.notNull(oid.getIdentifier(), "Object Identity doesn't provide an identifier");

        if (deleteChildren) {
            List<ObjectIdentity> children = findChildren(oid);
            if (children != null) {
                for (ObjectIdentity child : children) {
                    deleteAcl(child, true);
                }
            }
        }

        AclOid entity = oidRepository.findOne(oid)
            .orElseThrow(() -> new IllegalArgumentException("cannot found AclOid entity by ObjectIdentity: " + oid));
        List<AclEntry> entities = entryRepository.findAllByOid(entity);

        // Delete this ACL's ACEs in the acl_entry table
        entryRepository.deleteAll(entities);
        // Delete this ACL's acl_oid row
        oidRepository.delete(entity);
    }

    @Override
    public void updateAcl(MutableAcl acl) {
        Assert.notNull(acl.getId(), "Object Identity doesn't provide an identifier");
        Assert.notNull(acl.getOwner(), "Owner is required in this implementation");

        // Update this ACL's ACEs in the acl_entry table
        long oidId = (Long) acl.getId();
        List<AccessControlEntry> entries = acl.getEntries();
        List<Sid> sids = entries.stream().map(AccessControlEntry::getSid).toList();
        Set<AclSid> aclSids = saveAclSids(sids);
        AclOid aclOid = oidRepository.findById(oidId).orElseThrow();
        // ACE 아이디, ACE 엔터티 맵
        Map<Long, AclEntry> entityMap = entryRepository.findMap(aclOid);
        Map<AclSidKey, AclSid> sidMap = aclSids.stream().collect(Collectors.toMap(AclSid::getKey, Function.identity()));
        Set<AclEntry> savedAclEntries = IntStream.range(0, entries.size())
            .mapToObj(i -> Pair.of(entries.get(i), i))
            .map(pair -> saveAclEntry(pair.getFirst(), pair.getSecond(), entityMap, aclOid, sidMap))
            .collect(Collectors.toSet());
        entityMap.values().stream()
            .filter(aclEntry -> !savedAclEntries.contains(aclEntry))
            .forEach(entryRepository::delete);

        // Change the mutable columns in acl_oid
        Optional.ofNullable(acl.getParentAcl())
            .map(Acl::getObjectIdentity)
            .flatMap(oidRepository::findOne)
            .ifPresent(aclOid::setParent);
        AclSid aclSid = getOrCreateAclSid(acl.getOwner());
        aclOid.setOwnerSid(aclSid);
        aclOid.setEntriesInheritingType(AclOidEntriesInheritingType.SOFT);
        oidRepository.save(aclOid);
    }

    @Override
    public List<ObjectIdentity> findChildren(ObjectIdentity oid) {
        Assert.notNull(oid, "oid cannot be null");
        List<AclOid> children = oidRepository.findChildren(oid);
        return children.stream().map(oidFactory::createObjectIdentity).toList();
    }

    @Override
    public List<AclSource> findAclSourcesByObjectIdentityIn(Set<ObjectIdentity> oids) {
        List<AclSourceBasePart> baseParts = oidRepository.findAclSourceBasePartsByOids(oids);
        return findAclSources(baseParts);
    }

    @Override
    public List<AclSource> findAclSourcesByObjectIdentityIdIn(Set<Long> ids) {
        List<AclSourceBasePart> baseParts = oidRepository.findAclSourceBasePartsByIds(ids);
        return findAclSources(baseParts);
    }

    private AclSid getOrCreateAclSid(Sid sid) throws IllegalArgumentException {
        SidModel sidModel = SidModel.from(sid);
        AclSidKey key = new AclSidKey(sidModel.getType(), sidModel.getValue());
        return sidRepository.findByKey(key).orElseGet(() -> saveSid(key));
    }

    private AclSid saveSid(AclSidKey key) {
        Assert.notNull(key, "key cannot be null");
        Assert.notNull(key.getType(), "key.type cannot be null");
        Assert.hasText(key.getValue(), "key.value cannot be blank");
        AclSid entity = AclSid.builder().key(key).build();
        return sidRepository.save(entity);
    }

    private AclDomain saveAclDomain(String domainCode, @Nullable String idConverterName) {
        AclDomain entity = AclDomain.builder().code(domainCode).idConverter(idConverterName).build();
        return domainRepository.save(entity);
    }

    private Set<AclSid> saveAclSids(List<Sid> sids) {
        Set<AclSidKey> keys = sids.stream()
            .map(AclSidKey::from)
            .collect(Collectors.toUnmodifiableSet());
        Map<AclSidKey, AclSid> sidMap = sidRepository.findMapByKeyIn(keys);
        return keys.stream()
            .map(key -> Objects.requireNonNullElseGet(sidMap.get(key), () -> saveSid(key)))
            .collect(Collectors.toSet());
    }

    private AclEntry saveAclEntry(
        AccessControlEntry entry,
        int aceOrder,
        Map<Long, AclEntry> entityMap,
        AclOid aclOid,
        Map<AclSidKey, AclSid> sidMap
    ) {
        return entry.getId() != null
            ? updateAclEntity(entry, aceOrder, entityMap)
            : createAclEntity(entry, aceOrder, aclOid, sidMap);
    }

    private AclEntry updateAclEntity(AccessControlEntry entry, int aceOrder, Map<Long, AclEntry> entityMap) {
        AclEntry entity = Optional.ofNullable(entityMap.get((Long) entry.getId())).orElseThrow();
        entity.setOrder(aceOrder);
        entity.setMask(entry.getPermission().getMask());
        return entryRepository.save(entity);
    }

    private AclEntry createAclEntity(AccessControlEntry entry, int aceOrder, AclOid aclOid, Map<AclSidKey, AclSid> sidMap) {
        Assert.isTrue(entry instanceof CustomAce, "Unknown ACE class");
        CustomAce customAce = (CustomAce) entry;
        AclSidKey sidKey = AclSidKey.from(entry.getSid());
        AclSid aclSid = sidMap.get(sidKey);
        AclEntry entity = AclEntry.builder()
            .order(aceOrder)
            .mask(entry.getPermission().getMask())
            .granting(entry.isGranting())
            .auditSuccess(customAce.isAuditSuccess())
            .auditFailure(customAce.isAuditFailure())
            .oid(aclOid)
            .sid(aclSid)
            .build();
        return entryRepository.save(entity);
    }

    private List<AclSource> findAclSources(List<AclSourceBasePart> baseParts) {
        Set<Long> oidIds = baseParts.stream().map(AclSourceBasePart::getAclObjectId).collect(Collectors.toSet());
        Map<Long, List<AclSourceAcePart>> acePartsMap = entryRepository.findAclSourceAcePartsMap(oidIds);
        return baseParts.stream().flatMap(basePart -> toAclSourceStream(basePart, acePartsMap)).toList();
    }

    private Stream<AclSource> toAclSourceStream(AclSourceBasePart basePart, Map<Long, List<AclSourceAcePart>> acePartsMap) {
        List<AclSourceAcePart> aceParts = acePartsMap.getOrDefault(basePart.getAclObjectId(), Collections.emptyList());
        if (aceParts.isEmpty()) {
            return Stream.of(toAclSource(basePart, null));
        } else {
            return aceParts.stream().map(acePart -> toAclSource(basePart, acePart));
        }
    }

    private AclSource toAclSource(AclSourceBasePart basePart, @Nullable AclSourceAcePart acePart) {
        AclSource.AclSourceBuilder builder = AclSource.builder()
            .aclId(basePart.getAclObjectId())
            .aclParentId(basePart.getParentAclObjectId())
            .entriesInheriting(Optional.ofNullable(basePart.getEntriesInheriting()).orElse(false))
            .identifier(Optional.ofNullable(basePart.getDomainIdConverter())
                .flatMap(idConverterRepository::findByName)
                .map(converter -> converter.convert(basePart.getObjectIdentifier()))
                .orElse(basePart.getObjectIdentifier()))
            .type(basePart.getDomainCode())
            .aclSidType(basePart.getSidType())
            .aclSid(basePart.getSidValue());
        if (acePart != null) {
            builder
                .aceId(acePart.getId())
                .aceSid(acePart.getSid())
                .aceSidType(acePart.getSidType())
                .mask(Optional.ofNullable(acePart.getMask()).orElse(0))
                .granting(Optional.ofNullable(acePart.getGranting()).orElse(false))
                .auditSuccess(Optional.ofNullable(acePart.getAuditSuccess()).orElse(false))
                .auditFailure(Optional.ofNullable(acePart.getAuditFailure()).orElse(false));
        }
        return builder.build();
    }
}
