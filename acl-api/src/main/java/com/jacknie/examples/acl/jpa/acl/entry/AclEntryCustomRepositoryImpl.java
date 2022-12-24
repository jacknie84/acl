package com.jacknie.examples.acl.jpa.acl.entry;

import com.jacknie.examples.acl.jpa.acl.oid.AclOid;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.jacknie.examples.acl.jpa.acl.entry.QAclEntry.aclEntry;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@SuppressWarnings("unused")
public class AclEntryCustomRepositoryImpl extends QuerydslRepositorySupport implements AclEntryCustomRepository {

    private final QAclSourceAcePart aclSourceAcePart = new QAclSourceAcePart(
        aclEntry.id,
        aclEntry.sid.key.value,
        aclEntry.sid.key.type,
        aclEntry.mask,
        aclEntry.granting,
        aclEntry.auditSuccess,
        aclEntry.auditFailure
    );

    public AclEntryCustomRepositoryImpl() {
        super(AclEntry.class);
    }

    @Override
    public Map<Long, List<AclSourceAcePart>> findAclSourceAcePartsMap(Set<Long> objectIds) {
        if (CollectionUtils.isEmpty(objectIds)) {
            return Collections.emptyMap();
        } else {
            return from(aclEntry)
                .leftJoin(aclEntry.sid)
                .where(aclEntry.oid.id.in(objectIds))
                .transform(groupBy(aclEntry.oid.id).as(list(aclSourceAcePart)));
        }
    }

    @Override
    public Map<Long, AclEntry> findMap(AclOid oid) {
        return from(aclEntry)
            .where(aclEntry.oid.eq(oid))
            .transform(groupBy(aclEntry.id).as(aclEntry));
    }
}
