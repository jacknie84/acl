package com.jacknie.examples.acl.config.security.acls.service.impl;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.model.Permission;

import java.util.HashMap;
import java.util.Map;

public class AbbreviationPermissionFactory extends DefaultPermissionFactory {

    private final Map<String, Permission> registeredPermissionsByName = new HashMap<>();

    public AbbreviationPermissionFactory() {
        super();
        registeredPermissionsByName.put("R", BasePermission.READ);
        registeredPermissionsByName.put("W", BasePermission.WRITE);
        registeredPermissionsByName.put("C", BasePermission.CREATE);
        registeredPermissionsByName.put("D", BasePermission.DELETE);
        registeredPermissionsByName.put("A", BasePermission.ADMINISTRATION);
    }

    @Override
    public Permission buildFromName(String name) {
        Permission permission = registeredPermissionsByName.get(name);
        if (permission != null) {
            return permission;
        } else {
            return super.buildFromName(name);
        }
    }
}
