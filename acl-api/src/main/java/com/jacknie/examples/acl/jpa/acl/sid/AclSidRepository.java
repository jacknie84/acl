package com.jacknie.examples.acl.jpa.acl.sid;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AclSidRepository extends JpaRepository<AclSid, Long>, AclSidCustomRepository {

    boolean existsBySid(String sid);

}
