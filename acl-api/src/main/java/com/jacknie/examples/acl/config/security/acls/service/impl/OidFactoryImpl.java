package com.jacknie.examples.acl.config.security.acls.service.impl;

import com.jacknie.examples.acl.config.security.acls.domain.OidModel;
import com.jacknie.examples.acl.config.security.acls.service.AclDomainIdConverterRepository;
import com.jacknie.examples.acl.config.security.acls.domain.AclIdentifiable;
import com.jacknie.examples.acl.config.security.acls.service.AclDomainCodeRepository;
import com.jacknie.examples.acl.config.security.acls.service.OidFactory;
import com.jacknie.examples.acl.jpa.acl.oid.AclOid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.util.Assert;

import java.io.Serializable;

@RequiredArgsConstructor
public class OidFactoryImpl implements OidFactory {

    private final AclDomainIdConverterRepository idConverterRepository;
    private final AclDomainCodeRepository domainCodeRepository;

    @Override
    public ObjectIdentity createObjectIdentity(AclOid oid) {
        Assert.notNull(oid, "oid cannot be null");
        Assert.notNull(oid.getDomain(), "oid.domain cannot be null");
        Assert.hasText(oid.getDomain().getCode(), "oid.domain.code cannot be blank");
        String domainCode = oid.getDomain().getCode();
        String idConverterName = oid.getDomain().getIdConverter();
        Serializable identifier = oid.getIdentifier();
        if (idConverterName != null) {
            Converter<String, Serializable> converter = idConverterRepository.findByName(idConverterName)
                .orElseThrow(() -> new IllegalStateException("cannot found id converter by name: " + idConverterName));
            identifier = converter.convert(oid.getIdentifier());
        }
        return createObjectIdentity(identifier, domainCode);
    }

    @Override
    public ObjectIdentity createObjectIdentity(AclIdentifiable identifiable) {
        Assert.notNull(identifiable, "identifiable cannot be null");
        Assert.notNull(identifiable.getId(), "identifiable.id cannot be null");
        Serializable identifier = identifiable.getId();
        Class<? extends AclIdentifiable> type = identifiable.getClass();
        String domainCode = domainCodeRepository.findByType(type)
            .orElseThrow(() -> NotFoundRegisteredDomainException.byType(type));
        return createObjectIdentity(identifier, domainCode);
    }

    @Override
    public ObjectIdentity createObjectIdentity(Serializable identifier, String domainCode) {
        Assert.notNull(identifier, "identifier cannot be null");
        Assert.hasText(domainCode, "domainCode cannot be blank");
        boolean exists = domainCodeRepository.exists(domainCode);
        if (!exists) {
            throw NotFoundRegisteredDomainException.byCode(domainCode);
        }
        return new ObjectIdentityImpl(domainCode, identifier);
    }

    @Override
    public ObjectIdentity getObjectIdentity(Object domainObject) {
        Assert.isInstanceOf(AclIdentifiable.class, domainObject);
        return createObjectIdentity((AclIdentifiable) domainObject);
    }

    @Override
    public ObjectIdentity createObjectIdentity(OidModel oidModel) {
        Assert.notNull(oidModel, "oidModel cannot be null");
        Serializable identifier = oidModel.getIdentifier();
        String domainCode = oidModel.getType();
        return createObjectIdentity(identifier, domainCode);
    }
}
