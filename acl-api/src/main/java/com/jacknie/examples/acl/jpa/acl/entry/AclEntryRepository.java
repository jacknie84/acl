package com.jacknie.examples.acl.jpa.acl.entry;

import com.jacknie.examples.acl.jpa.acl.oid.AclOid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AclEntryRepository extends JpaRepository<AclEntry, Long>, AclEntryCustomRepository {

    /**
     * 객체 식별 정보에 해당하는 모든 ACE 목록을 조회
     * @param oid 객체 식별 정보
     * @return ACE 목록
     */
    List<AclEntry> findAllByOid(AclOid oid);
}
