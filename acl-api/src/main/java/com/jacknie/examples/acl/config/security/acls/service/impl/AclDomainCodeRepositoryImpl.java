package com.jacknie.examples.acl.config.security.acls.service.impl;

import com.jacknie.examples.acl.config.security.acls.service.AclDomainCodeRepository;
import com.jacknie.examples.acl.config.security.acls.domain.AclIdentifiable;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import java.util.*;

@RequiredArgsConstructor
public class AclDomainCodeRepositoryImpl implements AclDomainCodeRepository {

    private final Set<String> availableDomainCodes;
    private final Map<Class<? extends AclIdentifiable>, String> domainCodeMap;

    @Override
    public boolean exists(String domainCode) {
        return availableDomainCodes.contains(domainCode);
    }

    @Override
    public Optional<String> findByType(Class<? extends AclIdentifiable> type) {
        String domainCode = domainCodeMap.get(type);
        return Optional.ofNullable(domainCode);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Set<String> availableDomainCodes = new LinkedHashSet<>();
        private final Map<Class<? extends AclIdentifiable>, String> domainCodeMap = new LinkedHashMap<>();

        public Builder map(Class<? extends AclIdentifiable> type, String domainCode) {
            Assert.notNull(type, "type cannot be null");
            Assert.hasText(domainCode, "domainCode cannot be blank");
            Assert.isTrue(!domainCodeMap.containsKey(type), "already exists domain type: " + type);
            availableDomainCodes.add(domainCode);
            domainCodeMap.put(type, domainCode);
            return this;
        }

        public AclDomainCodeRepositoryImpl build() {
            return new AclDomainCodeRepositoryImpl(
                Collections.unmodifiableSet(availableDomainCodes),
                Collections.unmodifiableMap(domainCodeMap)
            );
        }
    }
}
