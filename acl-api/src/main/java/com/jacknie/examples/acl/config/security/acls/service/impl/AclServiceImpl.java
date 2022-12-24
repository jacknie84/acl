package com.jacknie.examples.acl.config.security.acls.service.impl;

import com.jacknie.examples.acl.config.security.acls.service.AclOperations;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.*;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AclServiceImpl implements AclService {

    @Override
    public List<ObjectIdentity> findChildren(ObjectIdentity oid) {
        return getAclOperations().findChildren(oid);
    }

    @Override
    public Acl readAclById(ObjectIdentity oid) throws NotFoundException {
        return readAclById(oid, Collections.emptyList());
    }

    @Override
    public Acl readAclById(ObjectIdentity oid, List<Sid> sids) throws NotFoundException {
        Map<ObjectIdentity, Acl> map = readAclsById(Collections.singletonList(oid), sids);
        Assert.isTrue(map.containsKey(oid),
            () -> "There should have been an Acl entry for ObjectIdentity " + oid);
        return map.get(oid);
    }

    @Override
    public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> oids) throws NotFoundException {
        return readAclsById(oids, Collections.emptyList());
    }

    @Override
    public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> oids, List<Sid> sids) throws NotFoundException {
        Map<ObjectIdentity, Acl> result = getLookupStrategy().readAclsById(oids, sids);
        // Check every requested object identity was found (throw NotFoundException if needed)
        for (ObjectIdentity oid : oids) {
            if (!result.containsKey(oid)) {
                throw new NotFoundException("Unable to find ACL information for object identity '" + oid + "'");
            }
        }
        return result;
    }

    /**
     * @return ACL 데이터 처리 명령 목록
     */
    protected abstract AclOperations getAclOperations();

    /**
     * @return ACL 데이터 검색
     */
    protected abstract LookupStrategy getLookupStrategy();
}
