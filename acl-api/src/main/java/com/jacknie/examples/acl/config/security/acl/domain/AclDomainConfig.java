package com.jacknie.examples.acl.config.security.acl.domain;

import com.jacknie.examples.acl.config.security.acl.AclIdentifiable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

@Getter
@RequiredArgsConstructor
public class AclDomainConfig {

    private final Map<String, AclDomain> domainByCode;
    private final Map<Class<? extends AclIdentifiable>, AclDomain> domainByType;
    private final ConversionService idConversionService;

    public Optional<AclDomain> findDomain(Class<? extends AclIdentifiable> type) {
        Assert.notNull(type, "type cannot be null");
        return Optional.ofNullable(domainByType.get(type));
    }

    public Optional<AclDomain> findDomain(String domainCode) {
        Assert.notNull(domainCode, "domainCode cannot be null");
        return Optional.ofNullable(domainByCode.get(domainCode));
    }

    public ConversionService getIdConversionService() {
        return idConversionService;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<AclDomain> domains = new ArrayList<>();
        private final GenericConversionService idConversionService = new GenericConversionService();

        public Builder addDomain(String domainCode, Class<? extends AclIdentifiable> type) {
            Assert.hasText(domainCode, "domainCode cannot be blank");
            Assert.notNull(type, "type cannot be null");
            Assert.isTrue(domains.stream().noneMatch(domain -> domain.getCode().equals(domainCode)), () -> "already exists domain code: " + domainCode);
            Assert.isTrue(domains.stream().noneMatch(domain -> domain.getType().equals(type)), () -> "already exists type: " + type);
            AclDomain domain = new AclDomain(type, domainCode);
            domains.add(domain);
            return this;
        }

        public <ID extends Serializable> Builder addIdConverter(Converter<String, ID> idConverter, Class<ID> idType) {
            Assert.notNull(idConverter, "idConverter cannot be null");
            Assert.notNull(idType, "idType cannot be null");
            idConversionService.addConverter(String.class, idType, idConverter);
            return this;
        }

        public AclDomainConfig build() {
            final Map<String, AclDomain> domainByCode = new LinkedHashMap<>();
            final Map<Class<? extends AclIdentifiable>, AclDomain> domainByType = new LinkedHashMap<>();
            for (AclDomain domain : domains) {
                domainByCode.put(domain.getCode(), domain);
                domainByType.put(domain.getType(), domain);
            }
            idConversionService.addConverter(String.class, String.class, string -> string);
            return new AclDomainConfig(domainByCode, domainByType, idConversionService);
        }
    }
}
