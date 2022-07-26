package com.jacknie.examples.acl.jpa.acl.entry;

import com.jacknie.examples.acl.jpa.acl.oid.AclObjectIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AclEntryRepository extends JpaRepository<AclEntry, Long>, AclEntryCustomRepository {

    /**
     * ACE 목록 조회
     * @param aclOid 객체 식별 정보 entity
     * @return ACE 목록
     */
    List<AclEntry> findAllByObjectIdentity(AclObjectIdentity aclOid);

    List<AclEntry> findAllByObjectIdentityId(long id);
}
