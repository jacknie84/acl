package com.jacknie.examples.acl.config.security.acls.service;

import org.springframework.core.convert.converter.Converter;

import java.io.Serializable;
import java.util.Optional;

public interface AclDomainIdConverterRepository {

    /**
     * 아이디 컨버터 이름 조회
     * @param idType 아이디 타입
     * @return 컨버터 이름
     */
    Optional<String> findName(Class<? extends Serializable> idType);

    /**
     * 아이디 컨버터 조회
     * @param name 아이디 컨버터 이름
     * @return 아이디 컨버터
     */
    Optional<Converter<String, Serializable>> findByName(String name);
}
