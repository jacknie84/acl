package com.jacknie.examples.acl.jpa.acl.oid;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.jacknie.examples.acl.jpa.acl.oid.QAclOid.aclOid;

@SuppressWarnings("unused")
public class AclOidCustomRepositoryImpl extends QuerydslRepositorySupport implements AclOidCustomRepository {

    private final QAclSourceBasePart aclSourceBasePart = new QAclSourceBasePart(
        aclOid.id,
        aclOid.parent.id,
        aclOid.entriesInheritingType.isNotNull(),
        aclOid.identifier,
        aclOid.domain.idConverter,
        aclOid.domain.code,
        aclOid.ownerSid.key.type,
        aclOid.ownerSid.key.value
    );

    public AclOidCustomRepositoryImpl() {
        super(AclOid.class);
    }

    @Override
    public boolean exists(ObjectIdentity oid) {
        Long existsId = from(aclOid).select(aclOid.id)
            .leftJoin(aclOid.domain)
            .where(
                aclOid.identifier.eq(oid.getIdentifier().toString()),
                aclOid.domain.code.eq(oid.getType())
            )
            .fetchFirst();
        return existsId != null;
    }

    @Override
    public List<AclSourceBasePart> findAclSourceBasePartsByOids(Set<ObjectIdentity> oids) {
        if (CollectionUtils.isEmpty(oids)) {
            return Collections.emptyList();
        } else {
            return selectFromAclSourceBasePart()
                .where(
                    oids.stream()
                        .map(oid -> aclOid.identifier.eq(oid.getIdentifier().toString())
                            .and(aclOid.domain.code.eq(oid.getType())))
                        .reduce(BooleanExpression::or)
                        .orElseThrow()
                )
                .fetch();
        }
    }

    @Override
    public List<AclSourceBasePart> findAclSourceBasePartsByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        } else {
            return selectFromAclSourceBasePart()
                .where(aclOid.id.in(ids))
                .fetch();
        }
    }

    @Override
    public Optional<AclOid> findOne(ObjectIdentity oid) {
        AclOid entity = from(aclOid)
            .where(
                aclOid.identifier.eq(oid.getIdentifier().toString()),
                aclOid.domain.code.eq(oid.getType())
            )
            .fetchOne();
        return Optional.ofNullable(entity);
    }

    @Override
    public List<AclOid> findChildren(ObjectIdentity oid) {
        return from(aclOid)
            .leftJoin(aclOid.parent)
            .leftJoin(aclOid.domain).fetchJoin()
            .where(
                aclOid.parent.identifier.eq(oid.getIdentifier().toString()),
                aclOid.parent.domain.code.eq(oid.getType())
            )
            .fetch();
    }

    private JPQLQuery<AclSourceBasePart> selectFromAclSourceBasePart() {
        return from(aclOid).select(aclSourceBasePart)
            .leftJoin(aclOid.ownerSid)
            .leftJoin(aclOid.domain)
            .leftJoin(aclOid.parent);
    }
}
