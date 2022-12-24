package com.jacknie.examples.acl.config.security.acls.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.security.acls.model.*;
import org.springframework.util.Assert;

import java.io.Serializable;

@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CustomAce implements AccessControlEntry, AuditableAccessControlEntry, AceModelFactory {

    private final Sid sid;
    private final boolean granting;

    @Nullable
    private Long id;

    private Acl acl;

    private Permission permission;

    private boolean auditSuccess;

    private boolean auditFailure;

    @Nullable
    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public Acl getAcl() {
        return acl;
    }

    @Override
    public Permission getPermission() {
        return permission;
    }

    @Override
    public Sid getSid() {
        return sid;
    }

    @Override
    public boolean isGranting() {
        return granting;
    }

    @Override
    public boolean isAuditSuccess() {
        return auditSuccess;
    }

    @Override
    public boolean isAuditFailure() {
        return auditFailure;
    }

    @Override
    public AceModel createModel() {
        return AceModel.builder()
            .id(id)
            .aclId(getAclId())
            .sid(SidModel.from(sid))
            .mask(permission.getMask())
            .granting(granting)
            .auditSuccess(auditSuccess)
            .auditFailure(auditFailure)
            .build();
    }

    private long getAclId() {
        Assert.state(acl instanceof CustomAcl, "acl must be an instance of CustomAcl");
        return (Long) ((CustomAcl) acl).getId();
    }
}
