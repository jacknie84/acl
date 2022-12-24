package com.jacknie.examples.acl.config.security.acls.cache;

import com.jacknie.examples.acl.config.integration.RedisAclCacheMessagingGateway;
import com.jacknie.examples.acl.config.security.acls.domain.AceModel;
import com.jacknie.examples.acl.config.security.acls.domain.AclModel;
import com.jacknie.examples.acl.config.security.acls.domain.CustomAce;
import com.jacknie.examples.acl.config.security.acls.domain.CustomAcl;
import com.jacknie.examples.acl.config.security.acls.service.OidFactory;
import com.jacknie.examples.acl.config.security.acls.service.SidFactory;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Builder
public class RedisAclCache extends AbstractAclCache {

    private final Cache cache;
    private final OidFactory oidFactory;
    private final SidFactory sidFactory;
    private final PermissionFactory permissionFactory;
    private final AclAuthorizationStrategy authorizationStrategy;
    private final PermissionGrantingStrategy grantingStrategy;
    private final RedisAclCacheMessagingGateway messagingGateway;

    @Override
    protected void evictFromCacheInternal(MutableAcl acl) {
        Assert.isInstanceOf(CustomAcl.class, acl);
        cache.evict(acl.getId());
        cache.evict(acl.getObjectIdentity());
        CustomAcl customAcl = (CustomAcl) acl;
        messagingGateway.publishAclCacheEviction(customAcl.createModel());
    }

    @Override
    protected void putInCacheInternal(MutableAcl acl) {
        CustomAcl customAcl = (CustomAcl) acl;
        AclModel aclModel = customAcl.createModel();
        cache.put(acl.getObjectIdentity(), aclModel);
        cache.put(acl.getId(), aclModel);
    }

    @Override
    protected MutableAcl getFromCacheInternal(Object key) {
        Cache.ValueWrapper valueWrapper = cache.get(key);
        if (valueWrapper == null) {
            return null;
        }
        AclModel aclModel = (AclModel) valueWrapper.get();
        if (aclModel == null) {
            return null;
        }
        CustomAcl.CustomAclBuilder builder = CustomAcl.builder();
        if (aclModel.getParentId() != null) {
            MutableAcl parentAcl = getFromCacheInternal(aclModel.getParentId());
            builder.parentAcl(parentAcl);
        }
        List<CustomAce> entries = toCustomAceList(aclModel);
        CustomAcl acl = builder
            .id(aclModel.getId())
            .oid(oidFactory.createObjectIdentity(aclModel.getOid()))
            .owner(sidFactory.createSid(aclModel.getOwner()))
            .entriesInheriting(aclModel.isEntriesInheriting())
            .authorizationStrategy(authorizationStrategy)
            .grantingStrategy(grantingStrategy)
            .build();
        entries.forEach(ace -> ace.setAcl(acl));
        acl.replaceAccessControlEntries(entries);
        return acl;
    }

    @Override
    public void clearCache() {
        cache.clear();
    }

    private List<CustomAce> toCustomAceList(AclModel aclModel) {
        List<CustomAce> entries = new ArrayList<>();
        if (!CollectionUtils.isEmpty(aclModel.getEntries())) {
            for (AceModel aceModel : aclModel.getEntries()) {
                entries.add(toCustomAce(aceModel));
            }
        }
        return entries;
    }

    private CustomAce toCustomAce(AceModel aceModel) {
        return CustomAce.builder()
            .id(aceModel.getId())
            .sid(sidFactory.createSid(aceModel.getSid()))
            .granting(aceModel.isGranting())
            .permission(permissionFactory.buildFromMask(aceModel.getMask()))
            .auditSuccess(aceModel.isAuditSuccess())
            .auditFailure(aceModel.isAuditFailure())
            .build();
    }
}
