package com.jacknie.examples.acl.jpa.acl.sid;

import org.springframework.data.util.Pair;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface AclSidCustomRepository {

    /**
     * 아이디 조회
     * @param type 타입
     * @param sid 이름
     * @return 아이디
     */
    Optional<Long> findIdByTypeAndSid(SidType type, String sid);

    /**
     * 보안 식별 정보 맵 조회
     * @param sidPairs first: 보안 식별 정보, second: 보안 식별 정보 타입
     * @return 보안 식별 정보 맵
     */
    Map<Pair<String, SidType>, AclSid> findMapBySidAndTypePairIn(Set<Pair<String, SidType>> sidPairs);

    /**
     * 보안 식별 정보 맵 조회
     * @param sidIds 아이디 목록
     * @return 보안 식별 정보 맵
     */
    Map<Pair<String, SidType>, AclSid> findMapByIdIn(Set<Long> sidIds);
}
