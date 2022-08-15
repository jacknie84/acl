package com.jacknie.examples.acl.config.security.acl.domain;

import com.jacknie.examples.acl.config.security.acl.AclIdentifiable;

import java.io.Serializable;

public enum AclAdmin implements AclIdentifiable {

    ACL("acl"),
    MEMBER("member"),
    BUILDING("building");

    private final String id;

    AclAdmin(String id) {
        this.id = id;
    }

    @Override
    public Serializable getId() {
        return id;
    }
}
