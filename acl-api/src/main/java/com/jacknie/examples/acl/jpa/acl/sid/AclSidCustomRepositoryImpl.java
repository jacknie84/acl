package com.jacknie.examples.acl.jpa.acl.sid;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.jacknie.examples.acl.jpa.acl.sid.QAclSid.aclSid;
import static com.querydsl.core.group.GroupBy.groupBy;

public class AclSidCustomRepositoryImpl extends QuerydslRepositorySupport implements AclSidCustomRepository {

    public AclSidCustomRepositoryImpl() {
        super(AclSid.class);
    }

    @Override
    public Optional<Long> findIdByTypeAndSid(SidType type, String sid) {
        return Optional.ofNullable(from(aclSid).select(aclSid.id)
            .where(
                aclSid.type.eq(type),
                aclSid.sid.eq(sid)
            )
            .fetchOne());
    }

    @Override
    public Map<Pair<String, SidType>, AclSid> findMapBySidAndTypePairIn(Set<Pair<String, SidType>> sidPairs) {
        if (CollectionUtils.isEmpty(sidPairs)) {
            return Collections.emptyMap();
        } else {
            return toAclSidMap(from(aclSid)
                .where(
                    sidPairs.stream()
                        .map(pair -> aclSid.sid.eq(pair.getFirst()).and(aclSid.type.eq(pair.getSecond())))
                        .reduce(BooleanExpression::or)
                        .orElseThrow()
                ));
        }
    }

    @Override
    public Map<Pair<String, SidType>, AclSid> findMapByIdIn(Set<Long> sidIds) {
        if (CollectionUtils.isEmpty(sidIds)) {
            return Collections.emptyMap();
        } else {
            return toAclSidMap(from(aclSid).where(aclSid.id.in(sidIds)));
        }
    }

    public Map<Pair<String, SidType>, AclSid> toAclSidMap(JPQLQuery<AclSid> query) {
        return query.transform(groupBy(aclSid.sid, aclSid.type).as(aclSid))
            .entrySet().stream()
            .map(entry -> Pair.of(Pair.of((String) entry.getKey().get(0), (SidType) entry.getKey().get(1)), entry.getValue()))
            .collect(Pair.toMap());
    }
}
