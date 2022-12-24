package com.jacknie.examples.acl.jpa.acl.sid;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.jacknie.examples.acl.jpa.acl.sid.QAclSid.aclSid;
import static com.querydsl.core.group.GroupBy.groupBy;

@SuppressWarnings("unused")
public class AclSidCustomRepositoryImpl extends QuerydslRepositorySupport implements AclSidCustomRepository {

    public AclSidCustomRepositoryImpl() {
        super(AclSid.class);
    }

    @Override
    public Map<AclSidKey, AclSid> findMapByKeyIn(Set<AclSidKey> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyMap();
        } else {
            return from(aclSid)
                .where(aclSid.key.in(keys))
                .transform(groupBy(aclSid.key).as(aclSid));
        }
    }

    @Override
    public Map<AclSidKey, AclSid> findMapByIdIn(Set<Long> sidIds) {
        if (CollectionUtils.isEmpty(sidIds)) {
            return Collections.emptyMap();
        } else {
            return from(aclSid)
                .where(aclSid.id.in(sidIds))
                .transform(groupBy(aclSid.key).as(aclSid));
        }
    }
}
