package com.jacknie.examples.acl.jpa.acl.constant;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Optional;

import static com.jacknie.examples.acl.jpa.acl.constant.QAclConstant.aclConstant;

@SuppressWarnings("unused")
public class AclConstantCustomRepositoryImpl extends QuerydslRepositorySupport implements AclConstantCustomRepository {

    public AclConstantCustomRepositoryImpl() {
        super(AclConstant.class);
    }

    @Override
    public Optional<AclConstant> findOne(AclConstantMeta meta) {
        AclConstant entity = from(aclConstant)
            .where(
                aclConstant.meta.eq(meta),
                aclConstant.building.isNull()
            )
            .fetchOne();
        return Optional.ofNullable(entity);
    }
}
