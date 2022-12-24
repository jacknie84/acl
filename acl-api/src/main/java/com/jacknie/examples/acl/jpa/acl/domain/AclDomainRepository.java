package com.jacknie.examples.acl.jpa.acl.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AclDomainRepository extends JpaRepository<AclDomain, Long>, AclDomainCustomRepository {

    /**
     * 도메인 코드에 대한 도메인 정보 조회
     * @param domainCode 도메인 코드
     * @return 도메인 정보
     */
    Optional<AclDomain> findByCode(String domainCode);
}
