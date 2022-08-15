package com.jacknie.examples.acl.jpa.acl.oid;

import com.jacknie.examples.acl.jpa.acl.sid.SidType;
import com.jacknie.examples.acl.jpa.member.MemberAccount;
import com.jacknie.examples.acl.web.acl.AclFilterDto;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jacknie.examples.acl.jpa.QuerydslPredicateUtils.ifEmptyNone;
import static com.jacknie.examples.acl.jpa.acl.entry.QAclEntry.aclEntry;
import static com.jacknie.examples.acl.jpa.acl.oid.QAclObjectIdentity.aclObjectIdentity;
import static com.jacknie.examples.acl.jpa.member.QMemberAccount.memberAccount;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.dsl.Expressions.anyOf;
import static com.querydsl.jpa.JPAExpressions.selectOne;

@SuppressWarnings("ALL")
public class AclObjectIdentityCustomRepositoryImpl extends QuerydslRepositorySupport implements AclObjectIdentityCustomRepository {

    private final QAclSourceBasePart aclSourceBasePart = new QAclSourceBasePart(
        aclObjectIdentity.id,
        aclObjectIdentity.parentObject.id,
        aclObjectIdentity.entriesInheriting,
        aclObjectIdentity.objectIdIdentity,
        aclObjectIdentity.objectIdClass.classIdType,
        aclObjectIdentity.objectIdClass.className,
        aclObjectIdentity.ownerSid.type,
        aclObjectIdentity.ownerSid.sid
    );

    private final QObjectIdentitySource objectIdentitySource = new QObjectIdentitySource(
        aclObjectIdentity.objectIdIdentity,
        aclObjectIdentity.objectIdClass.className,
        aclObjectIdentity.objectIdClass.classIdType
    );

    public AclObjectIdentityCustomRepositoryImpl() {
        super(AclObjectIdentity.class);
    }

    @Override
    public boolean existsByObjectIdentity(ObjectIdentity oid) {
        Long existsId = from(aclObjectIdentity).select(aclObjectIdentity.id)
            .leftJoin(aclObjectIdentity.objectIdClass)
            .where(
                aclObjectIdentity.objectIdIdentity.eq(oid.getIdentifier().toString()),
                aclObjectIdentity.objectIdClass.className.eq(oid.getType())
            )
            .fetchFirst();
        return existsId != null;
    }

    @Override
    public List<AclSourceBasePart> findAclSourceBasePartsByObjectIdentityIn(Set<ObjectIdentity> oids) {
        if (CollectionUtils.isEmpty(oids)) {
            return Collections.emptyList();
        } else {
            return getAclSourceBasePartQuery()
                .where(
                    oids.stream()
                        .map(oid -> aclObjectIdentity.objectIdIdentity.eq(oid.getIdentifier().toString()).and(aclObjectIdentity.objectIdClass.className.eq(oid.getType())))
                        .reduce(BooleanExpression::or)
                        .orElseThrow()
                )
                .fetch();
        }
    }

    @Override
    public List<AclSourceBasePart> findAclSourceBasePartsByObjectIdentityIdIn(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        } else {
            return getAclSourceBasePartQuery()
                .where(
                    ids.stream()
                        .map(aclObjectIdentity.id::eq)
                        .reduce(BooleanExpression::or)
                        .orElseThrow()
                )
                .fetch();
        }
    }

    @Override
    public Optional<AclObjectIdentity> findByObjectIdentity(ObjectIdentity oid) {
        return Optional.ofNullable(from(aclObjectIdentity)
            .where(
                aclObjectIdentity.objectIdIdentity.eq(oid.getIdentifier().toString()),
                aclObjectIdentity.objectIdClass.className.eq(oid.getType())
            )
            .fetchOne());
    }

    @Override
    public Map<Long, AclObjectIdentity> findMapByIdIn(Set<Long> oidIds) {
        if (CollectionUtils.isEmpty(oidIds)) {
            return Collections.emptyMap();
        } else {
            return from(aclObjectIdentity)
                .where(aclObjectIdentity.id.in(oidIds))
                .transform(groupBy(aclObjectIdentity.id).as(aclObjectIdentity));
        }
    }

    @Override
    public Optional<Long> findIdByObjectIdentity(ObjectIdentity oid) {
        return Optional.ofNullable(from(aclObjectIdentity).select(aclObjectIdentity.id)
            .where(
                aclObjectIdentity.objectIdIdentity.eq(oid.getIdentifier().toString()),
                aclObjectIdentity.objectIdClass.className.eq(oid.getType())
            )
            .fetchOne());
    }

    @Override
    public List<ObjectIdentitySource> findChildrenByObjectIdentity(ObjectIdentity oid) {
        return from(aclObjectIdentity).select(objectIdentitySource)
            .leftJoin(aclObjectIdentity.parentObject)
            .leftJoin(aclObjectIdentity.objectIdClass)
            .where(
                aclObjectIdentity.parentObject.objectIdIdentity.eq(oid.getIdentifier().toString()),
                aclObjectIdentity.parentObject.objectIdClass.className.eq(oid.getType())
            )
            .fetch();
    }

    @Override
    public Page<AclObjectIdentity> findAll(AclFilterDto dto, Pageable pageable) {
        JPQLQuery<AclObjectIdentity> query = from(aclObjectIdentity)
            .join(aclObjectIdentity.objectIdClass)
            .join(aclObjectIdentity.ownerSid)
            .where(toPredicates(dto));
        long total = query.fetchCount();
        if (total > 0) {
            List<AclObjectIdentity> content = getQuerydsl().applyPagination(pageable, query).fetch();
            return new PageImpl<>(content, pageable, total);
        } else {
            return Page.empty(pageable);
        }
    }

    private JPQLQuery<AclSourceBasePart> getAclSourceBasePartQuery() {
        return from(aclObjectIdentity).select(aclSourceBasePart)
            .leftJoin(aclObjectIdentity.ownerSid)
            .leftJoin(aclObjectIdentity.objectIdClass)
            .leftJoin(aclObjectIdentity.parentObject);
    }

    private Predicate[] toPredicates(AclFilterDto dto) {
        return new Predicate[] {
            ifEmptyNone(aclObjectIdentity.objectIdClass.className::in, dto.getDomainCodes()),
            ifEmptyNone(this::conditionalizeOwnerSid, BooleanExpression::or, dto.getOwnerSids()),
            ifEmptyNone(this::existsByEntrySids, dto.getEntrySids()),
            ifEmptyNone(this::existsByAccountIds, dto.getAccountIds())
        };
    }

    private BooleanExpression conditionalizeOwnerSid(AclFilterDto.Sid sid) {
        return aclObjectIdentity.ownerSid.type.eq(sid.getType()).and(aclObjectIdentity.ownerSid.sid.eq(sid.getSid()));
    }

    private BooleanExpression existsByEntrySids(Set<AclFilterDto.Sid> entrySids) {
        return selectOne().from(aclEntry)
            .innerJoin(aclEntry.sid)
            .where(
                aclEntry.objectIdentity.id.eq(aclObjectIdentity.id),
                ifEmptyNone(this::conditionalizeEntrySid, BooleanExpression::or, entrySids)
            )
            .exists();
    }

    private BooleanExpression conditionalizeEntrySid(AclFilterDto.Sid sid) {
        return aclEntry.sid.type.eq(sid.getType()).and(aclEntry.sid.sid.eq(sid.getSid()));
    }

    private BooleanExpression existsByAccountIds(Set<Long> accountIds) {
        List<MemberAccount> accounts = from(memberAccount)
            .leftJoin(memberAccount.roles).fetchJoin()
            .where(memberAccount.id.in(accountIds))
            .fetch();
        Set<AclFilterDto.Sid> sids = accounts.stream()
            .flatMap(account -> Stream.concat(
                Stream.of(AclFilterDto.Sid.builder().type(SidType.PRINCIPAL).sid(account.getId().toString()).build()),
                account.getRoles().stream().map(role -> AclFilterDto.Sid.builder().type(SidType.GRANTED_AUTHORITY).sid(role.name()).build())
            ))
            .collect(Collectors.toSet());
        return anyOf(
            ifEmptyNone(this::conditionalizeOwnerSid, BooleanExpression::or, sids),
            ifEmptyNone(this::existsByEntrySids, sids)
        );
    }
}
