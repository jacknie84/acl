package com.jacknie.examples.acl.config.security.acls.service.impl;

import com.jacknie.examples.acl.config.security.acls.service.AclDomainIdConverterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class AclDomainIdConverterRepositoryImpl implements AclDomainIdConverterRepository {

    private final Map<Class<? extends Serializable>, String> converterNameMap;
    private final Map<String, Converter<String, Serializable>> converterMap;

    @Override
    public Optional<String> findName(Class<? extends Serializable> idType) {
        String name = converterNameMap.get(idType);
        return Optional.ofNullable(name);
    }

    @Override
    public Optional<Converter<String, Serializable>> findByName(String name) {
        Converter<String, Serializable> converter = converterMap.get(name);
        return Optional.ofNullable(converter);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Map<Class<? extends Serializable>, String> converterNameMap = new LinkedHashMap<>();
        private final Map<String, Converter<String, Serializable>> converterMap = new LinkedHashMap<>();

        public <T extends Serializable> Builder map(String name, Class<T> idType, Converter<String, Serializable> converter) {
            Assert.hasText(name, "name cannot be blank");
            Assert.notNull(idType, "idType cannot be blank");
            Assert.notNull(converter, "converter cannot be null");
            Assert.isTrue(!converterNameMap.containsKey(idType), "already exists id type: " + idType);
            Assert.isTrue(!converterMap.containsKey(name), "already exists converter name: " + name);
            converterNameMap.put(idType, name);
            converterMap.put(name, converter);
            return this;
        }

        public AclDomainIdConverterRepositoryImpl build() {
            return new AclDomainIdConverterRepositoryImpl(
                Collections.unmodifiableMap(converterNameMap),
                Collections.unmodifiableMap(converterMap)
            );
        }
    }
}
