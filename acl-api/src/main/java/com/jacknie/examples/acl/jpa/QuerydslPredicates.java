package com.jacknie.examples.acl.jpa;

import com.jacknie.experimental.acl.support.SidHelper;
import com.jacknie.experimental.jpa.acl.AclEntryFilter;
import com.jacknie.experimental.jpa.acl.AclObjectFilter;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.util.Assert;

import java.util.List;

import static com.jacknie.examples.acl.jpa.QuerydslFunctions.bitand;
import static com.jacknie.experimental.jpa.acl.entry.QAclEntry.aclEntry;
import static com.jacknie.experimental.jpa.acl.oid.QAclObject.aclObject;
import static com.querydsl.jpa.JPAExpressions.selectOne;

public abstract class QuerydslPredicates {

    private QuerydslPredicates() {
        throw new UnsupportedOperationException();
    }

    public static BooleanExpression existsAclEntry(AclEntryFilter filter) {
        Assert.hasText(filter.getDomainCode(), "domainCode cannot be blank");
        Assert.notNull(filter.getPermission(), "permission cannot be null");
        Assert.notEmpty(filter.getSids(), "sids cannot be empty");
        filter.getSids().forEach(sid -> Assert.notNull(sid, "sids element cannot be null"));
        Assert.notNull(filter.getIdentify(), "identify cannot be null");

        List<SidHelper> sidHelpers = filter.getSids().stream().map(SidHelper::new).toList();
        return selectOne().from(aclEntry)
            .where(
                filter.getIdentify().apply(aclEntry.aclObject.identifier),
                aclEntry.aclObject.aclDomain.domainCode.eq(filter.getDomainCode()),
                aclEntry.sid.sidType.in(sidHelpers.stream().map(SidHelper::getType).toList()),
                aclEntry.sid.sidValue.in(sidHelpers.stream().map(SidHelper::getSid).toList()),
                aclEntry.granting.isTrue(),
                bitand(aclEntry.mask, filter.getPermission().getMask()).gt(0)
            )
            .exists();
    }

    public static BooleanExpression existsAclObject(AclObjectFilter filter) {
        Assert.hasText(filter.getDomainCode(), "domainCode cannot be blank");
        Assert.notEmpty(filter.getOwnerSids(), "ownerSids cannot be empty");
        filter.getOwnerSids().forEach(sid -> Assert.notNull(sid, "ownerSids element cannot be null"));
        Assert.notNull(filter.getIdentify(), "identify cannot be null");

        List<SidHelper> sidHelpers = filter.getOwnerSids().stream().map(SidHelper::new).toList();
        return selectOne().from(aclObject)
            .where(
                filter.getIdentify().apply(aclObject.identifier),
                aclObject.aclDomain.domainCode.eq(filter.getDomainCode()),
                aclObject.ownerSid.sidType.in(sidHelpers.stream().map(SidHelper::getType).toList()),
                aclObject.ownerSid.sidValue.in(sidHelpers.stream().map(SidHelper::getSid).toList())
            )
            .exists();
    }
}
