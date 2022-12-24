package com.jacknie.examples.acl.jpa.acl.domain;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@SuppressWarnings("unused")
public class AclDomainCustomRepositoryImpl extends QuerydslRepositorySupport implements AclDomainCustomRepository {

    public AclDomainCustomRepositoryImpl() {
        super(AclDomain.class);
    }
}
