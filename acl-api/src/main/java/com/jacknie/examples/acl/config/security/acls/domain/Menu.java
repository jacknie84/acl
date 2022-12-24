package com.jacknie.examples.acl.config.security.acls.domain;

import lombok.Data;

@Data
public class Menu implements AclIdentifiable {

    private Long id;

    private String code;

    private Long parentId;
}
