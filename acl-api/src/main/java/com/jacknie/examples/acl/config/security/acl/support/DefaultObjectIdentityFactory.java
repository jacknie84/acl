package com.jacknie.examples.acl.config.security.acl.support;

import com.jacknie.examples.acl.config.security.acl.AclDomainConfig;
import com.jacknie.examples.acl.config.security.acl.AclIdentifiable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class DefaultObjectIdentityFactory implements ObjectIdentityFactory {

    private final AclDomainConfig domainConfig;

    @Override
    public ObjectIdentity createObjectIdentity(AclIdentifiable target) {
        Assert.notNull(target, "target cannot be null");
        Assert.notNull(target.getId(), "target.id cannot be null");
        Serializable id = target.getId();
        return domainConfig.findDomain(target.getClass())
            .map(domain -> new ObjectIdentityImpl(domain.getCode(), id))
            .orElseThrow(() -> new IllegalStateException("unsupported domain type: " + target.getClass()));
    }

    @Override
    public ObjectIdentity createObjectIdentity(Serializable identifier, String domainCode) {
        Assert.notNull(identifier, "identifier cannot be null");
        Assert.hasText(domainCode, "domainCode cannot be blank");
        return domainConfig.findDomain(domainCode)
            .map(domain -> new ObjectIdentityImpl(domain.getCode(), identifier))
            .orElseThrow(() -> new IllegalStateException("unsupported domain code: " + domainCode));
    }
}
