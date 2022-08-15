package com.jacknie.examples.acl.config.security.acl;

import lombok.Data;

@Data
public class AclDomain {

    private final Class<? extends AclIdentifiable> type;

    private final String code;

}
