package com.jacknie.examples.acl.config.security.acess.expression;

import com.jacknie.examples.acl.config.security.acls.AclExpressionOperations;
import com.jacknie.examples.acl.config.security.acls.CustomAclPermissionEvaluator;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

public class CustomSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations, AclExpressionOperations {

    private final Object target;
    private CustomAclPermissionEvaluator permissionEvaluator;
    private Object filterObject;
    private Object returnObject;

    public CustomSecurityExpressionRoot(Authentication authentication, Object target) {
        super(authentication);
        this.target = target;
    }

    @Override
    public void setPermissionEvaluator(PermissionEvaluator permissionEvaluator) {
        Assert.isInstanceOf(CustomAclPermissionEvaluator.class, permissionEvaluator);
        super.setPermissionEvaluator(permissionEvaluator);
        this.permissionEvaluator = (CustomAclPermissionEvaluator) permissionEvaluator;
    }

    @Override
    public boolean hasCreatePermission(String domainCode) {
        return permissionEvaluator.hasCreatePermission(authentication, domainCode);
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return target;
    }
}
