package com.jacknie.examples.acl.config.security.acls.hierarchy.impl;

import com.jacknie.examples.acl.config.security.acls.hierarchy.AclHierarchyKeeperComponent;
import com.jacknie.examples.acl.config.security.acls.service.AclServiceTemplate;
import com.jacknie.examples.acl.config.security.acls.service.AppendAceParams;
import com.jacknie.examples.acl.config.security.acls.service.SidFactory;
import com.jacknie.examples.acl.jpa.acl.constant.AclConstant;
import com.jacknie.examples.acl.jpa.acl.constant.AclConstantType;
import com.jacknie.examples.acl.jpa.member.MemberRole;
import com.jacknie.experimental.acl.domain.AclPermission;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.Sid;

import java.util.List;

@RequiredArgsConstructor
@Builder
public class RootAclHierarchyKeeperComponent implements AclHierarchyKeeperComponent {

    private final AclServiceTemplate aclServiceTemplate;
    private final SidFactory sidFactory;

    @Override
    public boolean isSupport(AclConstant constant) {
        return constant.getMeta().getType().equals(AclConstantType.ROOT) && constant.getBuilding() == null;
    }

    @Override
    public MutableAcl keepAces(AclConstant constant) {
        Sid sid = sidFactory.createSid(MemberRole.ROLE_ADMIN);
        MutableAcl acl = aclServiceTemplate.getOrCreateAcl(constant, sid, null);
        if (acl.isGranted(List.of(AclPermission.ALL), List.of(sid), true)) {
            return acl;
        }
        AppendAceParams params = AppendAceParams.builder()
            .acl(acl)
            .permission(AclPermission.ALL)
            .sid(sid)
            .auditSuccess(true)
            .auditFailure(true)
            .build();
        aclServiceTemplate.appendAce(params);
        return acl;
    }
}
