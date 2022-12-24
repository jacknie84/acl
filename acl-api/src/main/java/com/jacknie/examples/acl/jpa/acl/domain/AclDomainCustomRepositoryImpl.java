package com.jacknie.examples.acl.jpa.acl.type;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Optional;

import static com.jacknie.examples.acl.jpa.acl.type.QAclClass.aclClass;

public class AclClassCustomRepositoryImpl extends QuerydslRepositorySupport implements AclClassCustomRepository {

    public AclClassCustomRepositoryImpl() {
        super(AclClass.class);
    }

    @Override
    public Optional<Long> findIdByType(String type) {
        return Optional.ofNullable(from(aclClass).select(aclClass.id)
            .where(aclClass.className.eq(type))
            .fetchOne());
    }
}
