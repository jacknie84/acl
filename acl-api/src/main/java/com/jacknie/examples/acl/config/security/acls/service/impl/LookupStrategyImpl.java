package com.jacknie.examples.acl.config.security.acls.service.impl;

import com.jacknie.examples.acl.config.security.acls.domain.AclSource;
import com.jacknie.examples.acl.config.security.acls.domain.CustomAce;
import com.jacknie.examples.acl.config.security.acls.domain.CustomAcl;
import com.jacknie.examples.acl.config.security.acls.service.LookupOperations;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.*;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

@RequiredArgsConstructor
@Builder
public class LookupStrategyImpl implements LookupStrategy {

    private final int batchSize = 50;

    private final AclCache aclCache;
    private final PermissionFactory permissionFactory;
    private final LookupOperations lookupOperations;
    private final AclAuthorizationStrategy authorizationStrategy;
    private final PermissionGrantingStrategy grantingStrategy;

    @Override
    public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> oids, List<Sid> sids) {
        Assert.notEmpty(oids, "oids cannot be empty");
        // Map<ObjectIdentity, Acl>
        // contains FULLY loaded Acl objects
        Map<ObjectIdentity, Acl> result = new LinkedHashMap<>();
        Set<ObjectIdentity> currentBatchToLoad = new LinkedHashSet<>();
        for (int i = 0; i < oids.size(); i++) {
            ObjectIdentity oid = oids.get(i);
            // Check we don't already have this ACL in the results
            boolean aclFound = result.containsKey(oid);
            // Check cache for the present ACL entry
            if (!aclFound) {
                Acl acl = aclCache.getFromCache(oid);
                // Ensure any cached element supports all the requested SIDs
                // (they should always, as our base impl doesn't filter on SID)
                if (acl != null) {
                    Assert.state(acl.isSidLoaded(sids),
                        "Error: SID-filtered element detected when implementation does not perform SID filtering "
                            + "- have you added something to the cache manually?");
                    result.put(acl.getObjectIdentity(), acl);
                    aclFound = true;
                }
            }
            // Load the ACL from the database
            if (!aclFound) {
                currentBatchToLoad.add(oid);
            }
            // Is it time to load from JDBC the currentBatchToLoad?
            if ((currentBatchToLoad.size() == batchSize) || ((i + 1) == oids.size())) {
                if (currentBatchToLoad.size() > 0) {
                    Map<ObjectIdentity, Acl> loadedBatch = lookupObjectIdentities(currentBatchToLoad, sids);
                    // Add loaded batch (all elements 100% initialized) to results
                    result.putAll(loadedBatch);
                    // Add the loaded batch to the cache
                    for (Acl loadedAcl : loadedBatch.values()) {
                        aclCache.putInCache((MutableAcl) loadedAcl);
                    }
                    currentBatchToLoad.clear();
                }
            }
        }
        return result;
    }

    private Map<ObjectIdentity, Acl> lookupObjectIdentities(Set<ObjectIdentity> oids, List<Sid> sids) {
        Assert.notEmpty(oids, "Must provide identities to lookup");

        // contains Acls with StubAclParents
        Map<Serializable, Acl> acls = new HashMap<>();

        // Make the "acls" map contain all requested objectIdentities
        // (including markers to each parent in the hierarchy)
        List<AclSource> sources = lookupOperations.findAclSourcesByObjectIdentityIn(oids);
        Set<Long> parentsToLookup = processAclSources(sids, acls, sources);

        // Lookup the parents, now that our JdbcTemplate has released the database
        // connection (SEC-547)
        if (parentsToLookup.size() > 0) {
            lookupPrimaryKeys(acls, parentsToLookup, sids);
        }

        // Finally, convert our "acls" containing StubAclParents into true Acls
        Map<ObjectIdentity, Acl> resultMap = new HashMap<>();
        for (Acl inputAcl : acls.values()) {
            Assert.isInstanceOf(CustomAcl.class, inputAcl, "Map should have contained an CustomAcl");
            Assert.isInstanceOf(Long.class, ((CustomAcl) inputAcl).getId(), "Acl.getId() must be Long");
            Acl result = convert(acls, (Long) ((CustomAcl) inputAcl).getId());
            resultMap.put(result.getObjectIdentity(), result);
        }

        return resultMap;
    }

    private Set<Long> processAclSources(List<Sid> sids, Map<Serializable, Acl> acls, List<AclSource> sources) {
        Set<Long> parentIdsToLookup = new HashSet<>(); // Set of parent_id Longs

        for (AclSource source : sources) {
            // Convert current row into an Acl (albeit with a StubAclParent)
            convertCurrentResultIntoObject(acls, source);

            // Figure out if this row means we need to look up another parent
            Long parentId = source.getAclParentId();

            if (parentId != null) {
                // See if it's already in the "acls"
                if (acls.containsKey(parentId)) {
                    continue; // skip this while iteration
                }

                // Now try to find it in the cache
                MutableAcl cached = aclCache.getFromCache(parentId);
                if ((cached == null) || !cached.isSidLoaded(sids)) {
                    parentIdsToLookup.add(parentId);
                }
                else {
                    // Pop into the acls map, so our convert method doesn't
                    // need to deal with an unsynchronized AclCache
                    acls.put(cached.getId(), cached);
                }
            }
        }

        // Return the parents left to look up to the caller
        return parentIdsToLookup;
    }

    private void convertCurrentResultIntoObject(Map<Serializable, Acl> acls, AclSource source) {
        long id = source.getAclId();

        // If we already have an ACL for this ID, just create the ACE
        Acl acl = acls.get(id);

        if (acl == null) {
            ObjectIdentity oid = new ObjectIdentityImpl(source.getType(), source.getIdentifier());
            Sid owner = source.getAclSidType().createSid(source.getAclSid());

            Acl parentAcl = null;
            if (source.getAclParentId() != null) {
                parentAcl = new StubAclParent(source.getAclParentId());
            }

            acl = CustomAcl.builder()
                .id(id)
                .oid(oid)
                .parentAcl(parentAcl)
                .entriesInheriting(source.isEntriesInheriting())
                .owner(owner)
                .authorizationStrategy(authorizationStrategy)
                .grantingStrategy(grantingStrategy)
                .build();

            acls.put(id, acl);
        }

        // Add an extra ACE to the ACL (ORDER BY maintains the ACE list order)
        // It is permissible to have no ACEs in an ACL (which is detected by a null
        // ACE_SID)
        if (source.getAceSid() != null) {
            long aceId = source.getAceId();
            Sid recipient = source.getAceSidType().createSid(source.getAceSid());

            int mask = source.getMask();
            Permission permission = permissionFactory.buildFromMask(mask);

            CustomAce ace = CustomAce.builder()
                .id(aceId)
                .acl(acl)
                .sid(recipient)
                .permission(permission)
                .granting(source.isGranting())
                .auditSuccess(source.isAuditSuccess())
                .auditFailure(source.isAuditFailure())
                .build();

            if (!acl.getEntries().contains(ace)) {
                List<CustomAce> aces = new ArrayList<>();
                acl.getEntries().stream().map(CustomAce.class::cast).forEach(aces::add);
                aces.add(ace);
                ((CustomAcl) acl).replaceAccessControlEntries(aces);
            }
        }
    }

    /**
     * Locates the primary key IDs specified in "findNow", adding CustomAcl instances with
     * StubAclParents to the "acls" Map.
     * @param acls the CustomAcls (with StubAclParents)
     * @param findNow Long-based primary keys to retrieve
     * @param sids SIDs
     */
    private void lookupPrimaryKeys(Map<Serializable, Acl> acls, Set<Long> findNow, List<Sid> sids) {
        Assert.notNull(acls, "ACLs are required");
        Assert.notEmpty(findNow, "Items to find now required");
        List<AclSource> sources = lookupOperations.findAclSourcesByObjectIdentityIdIn(findNow);
        Set<Long> parentsToLookup = processAclSources(sids, acls, sources);
        // Lookup the parents, now that our JdbcTemplate has released the database
        // connection (SEC-547)
        if (parentsToLookup.size() > 0) {
            lookupPrimaryKeys(acls, parentsToLookup, sids);
        }
    }

    /**
     * The final phase of converting the <code>Map</code> of <code>CustomAcl</code>
     * instances which contain <code>StubAclParent</code>s into proper, valid
     * <code>CustomAcl</code>s with correct ACL parents.
     * @param inputMap the unconverted <code>CustomAcl</code>s
     * @param currentIdentity the current<code>Acl</code> that we wish to convert (this
     * may be
     */
    private CustomAcl convert(Map<Serializable, Acl> inputMap, Long currentIdentity) {
        Assert.notEmpty(inputMap, "InputMap required");
        Assert.notNull(currentIdentity, "CurrentIdentity required");

        // Retrieve this Acl from the InputMap
        Acl acl = inputMap.get(currentIdentity);
        Assert.isInstanceOf(CustomAcl.class, acl, "The inputMap contained a non-CustomAcl");

        CustomAcl inputAcl = (CustomAcl) acl;

        Acl parent = inputAcl.getParentAcl();

        if (parent instanceof StubAclParent stubAclParent) {
            // Lookup the parent
            parent = convert(inputMap, stubAclParent.getId());
        }

        // Now we have the parent (if there is one), create the true CustomAcl
        CustomAcl result = CustomAcl.builder()
            .oid(inputAcl.getObjectIdentity())
            .id((Long) inputAcl.getId())
            .authorizationStrategy(authorizationStrategy)
            .grantingStrategy(grantingStrategy)
            .parentAcl(parent)
            .entriesInheriting(inputAcl.isEntriesInheriting())
            .owner(inputAcl.getOwner())
            .build();

        // Copy the "aces" from the input to the destination

        // Obtain the "aces" from the input ACL
        List<CustomAce> aces = inputAcl.getEntries().stream().map(CustomAce.class::cast).toList();

        // Create a list in which to store the "aces" for the "result" CustomAcl instance
        List<CustomAce> acesNew = new ArrayList<>();

        // Iterate over the "aces" input and replace each nested
        // CustomAce.getAcl() with the new "result" CustomAcl instance
        // This ensures StubAclParent instances are removed, as per SEC-951
        for (CustomAce ace : aces) {
            ace.setAcl(result);
            acesNew.add(ace);
        }

        // Finally, now that the "aces" have been converted to have the "result" CustomAcl
        // instance, modify the "result" CustomAcl instance
        result.replaceAccessControlEntries(acesNew);

        return result;
    }

    private static class StubAclParent implements Acl {

        private final Long id;

        StubAclParent(Long id) {
            this.id = id;
        }

        Long getId() {
            return this.id;
        }

        @Override
        public List<AccessControlEntry> getEntries() {
            throw new UnsupportedOperationException("Stub only");
        }

        @Override
        public ObjectIdentity getObjectIdentity() {
            throw new UnsupportedOperationException("Stub only");
        }

        @Override
        public Sid getOwner() {
            throw new UnsupportedOperationException("Stub only");
        }

        @Override
        public Acl getParentAcl() {
            throw new UnsupportedOperationException("Stub only");
        }

        @Override
        public boolean isEntriesInheriting() {
            throw new UnsupportedOperationException("Stub only");
        }

        @Override
        public boolean isGranted(List<Permission> permission, List<Sid> sids, boolean administrativeMode)
            throws NotFoundException, UnloadedSidException {
            throw new UnsupportedOperationException("Stub only");
        }

        @Override
        public boolean isSidLoaded(List<Sid> sids) {
            throw new UnsupportedOperationException("Stub only");
        }

    }
}
