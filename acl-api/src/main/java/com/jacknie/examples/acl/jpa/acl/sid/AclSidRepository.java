package com.jacknie.examples.acl.jpa.acl.sid;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AclSidRepository extends JpaRepository<AclSid, Long>, AclSidCustomRepository {

    /**
     * 보안 식별 키에 해당하는 보안 식별 정보 조회
     * @param key 보안 식별 키
     * @return 보안 식별 정보
     */
    Optional<AclSid> findByKey(AclSidKey key);
}
