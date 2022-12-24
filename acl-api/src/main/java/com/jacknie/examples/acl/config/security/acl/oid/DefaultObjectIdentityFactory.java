package com.jacknie.examples.acl.config.security.acl.meta.oid;

import com.jacknie.examples.acl.config.security.acl.AclIdentifiable;
import com.jacknie.examples.acl.config.security.acl.domain.AclDomainRepository;
import com.jacknie.examples.acl.jpa.acl.oid.AclObject;
import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class DefaultObjectIdentityFactory implements ObjectIdentityFactory {

    private final Set<String> availableDomainCodes;
    private final Map<Class<? extends AclIdentifiable>, String> domainCodeMap;

    @Override
    public ObjectIdentity createObjectIdentity(AclIdentifiable target) {
        Assert.notNull(target, "target cannot be null");
        Assert.notNull(target.getId(), "target.id cannot be null");
        Serializable id = target.getId();
        String domainCode = domainCodeMap.get(target.getClass());
        if (!StringUtils.hasText(domainCode)) {
            String format = "객체 클래스(%s)로 등록 된 도메인 객체를 찾을 수 없습니다.";
            String message = String.format(format, target.getClass());
            throw new IllegalStateException(message);
        }
        return new ObjectIdentityImpl(domainCode, id);
    }

    @Override
    public ObjectIdentity createObjectIdentity(Serializable identifier, String domainCode) {
        Assert.notNull(identifier, "identifier cannot be null");
        Assert.hasText(domainCode, "domainCode cannot be blank");
        if (!availableDomainCodes.contains(domainCode)) {
            String format = "도메인 코드(%s)로 등록 된 도메인 객체를 찾을 수 없습니다.";
            String message = String.format(format, domainCode);
            throw new IllegalStateException(message);
        }
        return new ObjectIdentityImpl(domainCode, identifier);
    }

    @Override
    public ObjectIdentity createObjectIdentity(AclObject aclObject) {
        Assert.notNull(aclObject, "aclObject cannot be null");
        Serializable identifier = aclObject.getIdentifier();
        String domainCode = aclObject.getAclDomain().getDomainCode();
        return createObjectIdentity(identifier, domainCode);
    }
}
