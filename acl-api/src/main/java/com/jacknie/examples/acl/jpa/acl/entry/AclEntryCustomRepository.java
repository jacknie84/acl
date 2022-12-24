package com.jacknie.examples.acl.jpa.acl.entry;

import com.jacknie.examples.acl.jpa.acl.oid.AclOid;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AclEntryCustomRepository {

    /**
     * ACL 소스 데이터 ACE 파트 정보 목록 맵 조회
     * @param objectIds 객체 식별 정보 아이디 목록
     * @return ACE 파트 정보 목록 맵
     */
    Map<Long, List<AclSourceAcePart>> findAclSourceAcePartsMap(Set<Long> objectIds);

    /**
     * ACE 아이디, ACE 정보 맵 조회
     * @param oid 객체 식별 정보 entity
     * @return ACE 정보 맵
     */
    Map<Long, AclEntry> findMap(AclOid oid);
}
