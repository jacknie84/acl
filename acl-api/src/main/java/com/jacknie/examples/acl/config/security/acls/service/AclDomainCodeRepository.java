package com.jacknie.examples.acl.config.security.acls.service;

import com.jacknie.examples.acl.config.security.acls.domain.AclIdentifiable;

import java.util.Optional;

public interface AclDomainCodeRepository {

    /**
     * 해당 도메인 코드가 유효 한지 확인
     * @param domainCode 도메인 코드
     * @return 유효성 여부
     */
    boolean exists(String domainCode);

    /**
     * 도메인 타입에 해당하는 도메인 코드를 조회
     * @param type 도메인 타입
     * @return 도메인 코드
     */
    Optional<String> findByType(Class<? extends AclIdentifiable> type);
}
