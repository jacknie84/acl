package com.jacknie.examples.acl.config.security.acls;

import com.jacknie.examples.acl.config.security.acls.hierarchy.AclHierarchyKeeper;
import com.jacknie.examples.acl.config.security.acls.service.AclConstantService;
import com.jacknie.examples.acl.jpa.acl.constant.AclConstant;
import com.jacknie.experimental.acl.domain.AclPermission;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

@Setter
public class AclPermissionEvaluatorImpl extends AclPermissionEvaluator implements CustomAclPermissionEvaluator, InitializingBean {

    private AclConstantService constantService;
    private AclHierarchyKeeper hierarchyKeeper;

    public AclPermissionEvaluatorImpl(AclService aclService) {
        super(aclService);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(constantService, "constantService cannot be null");
        Assert.notNull(hierarchyKeeper, "hierarchyKeeper cannot be null");
    }

    @Override
    public boolean hasCreatePermission(Authentication authentication, String domainCode) {
        AclConstant domain = constantService.getOrCreateDomain(domainCode);
        hierarchyKeeper.initialize(domain);
        return hasPermission(authentication, domain, AclPermission.CREATE);
    }
}
