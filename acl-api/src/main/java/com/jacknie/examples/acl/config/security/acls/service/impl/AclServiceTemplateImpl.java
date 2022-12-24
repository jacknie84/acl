package com.jacknie.examples.acl.config.security.acls.service.impl;

import com.jacknie.examples.acl.config.security.acls.domain.AclIdentifiable;
import com.jacknie.examples.acl.config.security.acls.service.AclServiceTemplate;
import com.jacknie.examples.acl.config.security.acls.service.AppendAceParams;
import com.jacknie.examples.acl.config.security.acls.service.OidFactory;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.acls.model.*;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Builder
public class AclServiceTemplateImpl implements AclServiceTemplate {

    private final OidFactory oidFactory;
    private final MutableAclService aclService;

    @Override
    public MutableAcl getOrCreateAcl(AclIdentifiable identifiable, Sid owner, @Nullable Acl parent) {
        Assert.notNull(identifiable, "identifiable cannot be null");
        Assert.notNull(owner, "owner cannot be null");
        ObjectIdentity oid = oidFactory.createObjectIdentity(identifiable);
        return getOrCreateAcl(oid, owner, parent);
    }

    @Override
    public void appendAce(AppendAceParams params) {
        Assert.notNull(params, "params cannot be null");
        Assert.notNull(params.getAcl(), "params.acl cannot be null");
        Assert.notNull(params.getPermission(), "params.permission cannot be null");
        Assert.notNull(params.getSid(), "params.sid cannot be null");
        MutableAcl acl = params.getAcl();
        Permission permission = params.getPermission();
        Sid sid = params.getSid();
        int nextIndex = getNextAceIndex(acl);
        acl.insertAce(nextIndex, permission, sid, true);
        AuditableAcl auditableAcl = (AuditableAcl) acl;
        if (params.getAuditSuccess() != null || params.getAuditFailure() != null) {
            Assert.isInstanceOf(AuditableAcl.class, acl);
            boolean auditSuccess = params.getAuditSuccess() != null ? params.getAuditSuccess() : false;
            boolean auditFailure = params.getAuditFailure() != null ? params.getAuditFailure() : false;
            auditableAcl.updateAuditing(nextIndex, auditSuccess, auditFailure);
        }
        aclService.updateAcl(acl);
    }

    private MutableAcl getOrCreateAcl(ObjectIdentity oid, Sid owner, @Nullable Acl parent) {
        try {
            MutableAcl acl = (MutableAcl) aclService.readAclById(oid);
            Assert.state(acl.getOwner().equals(owner), () -> String.format("different owner(read: %s, pass: %s)", acl.getOwner(), owner));
            if (acl.getParentAcl() != null && parent != null) {
                Assert.state(acl.getParentAcl().equals(parent), () -> String.format("different parent(read: %s, pass: %s)", acl.getParentAcl(), parent));
            }
            return acl;
        } catch (NotFoundException e) {
            log.info(e.getMessage());
            MutableAcl acl = aclService.createAcl(oid);
            acl.setOwner(owner);
            acl.setParent(parent);
            aclService.updateAcl(acl);
            return acl;
        }
    }

    private int getNextAceIndex(MutableAcl acl) {
        List<AccessControlEntry> aces = acl.getEntries();
        if (CollectionUtils.isEmpty(aces)) {
            return 0;
        } else {
            return aces.size();
        }
    }

}
