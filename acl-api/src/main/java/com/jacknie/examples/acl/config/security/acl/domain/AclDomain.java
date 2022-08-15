package com.jacknie.examples.acl.config.security.acl.domain;

import com.jacknie.examples.acl.config.security.acl.AclIdentifiable;
import lombok.Data;

@Data
public class AclDomain {

    private final Class<? extends AclIdentifiable> type;

    private final String code;

}
