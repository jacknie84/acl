package com.jacknie.examples.acl.jpa.member;

import org.springframework.security.core.GrantedAuthority;

public enum MemberRole implements GrantedAuthority {

    ROLE_USER, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
