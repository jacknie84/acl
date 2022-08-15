package com.jacknie.examples.acl.config.security.acl.support;

import com.jacknie.examples.acl.config.security.acl.AclIdentifiable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityGenerator;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;
import org.springframework.util.Assert;

import java.io.Serializable;

@RequiredArgsConstructor
public class ObjectIdentityConverter implements ObjectIdentityRetrievalStrategy, ObjectIdentityGenerator {

    private final ObjectIdentityFactory oidFactory;

    @Override
    public ObjectIdentity createObjectIdentity(Serializable id, String type) {
        return null;
    }

    @Override
    public ObjectIdentity getObjectIdentity(Object domainObject) {
        Assert.isInstanceOf(AclIdentifiable.class, domainObject);
        if (domainObject instanceof AclIdentifiable identifiable) {
            return oidFactory.createObjectIdentity(identifiable);
        } else {
            throw new AssertionError("cannot reachable");
        }
    }
}
