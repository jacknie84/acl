package com.jacknie.examples.acl.jpa.acl.sid;

import java.util.Map;
import java.util.Set;

public interface AclSidCustomRepository {

    /**
     * 보안 식별 정보 맵 조회
     * @param keys 보안 식별 정보 키 목록
     * @return 보안 식별 정보 맵
     */
    Map<AclSidKey, AclSid> findMapByKeyIn(Set<AclSidKey> keys);

    /**
     * 보안 식별 정보 맵 조회
     * @param sidIds 아이디 목록
     * @return 보안 식별 정보 맵
     */
    Map<AclSidKey, AclSid> findMapByIdIn(Set<Long> sidIds);
}
