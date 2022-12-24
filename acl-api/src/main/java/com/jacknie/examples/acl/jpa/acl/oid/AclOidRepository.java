package com.jacknie.examples.acl.jpa.acl.oid;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AclOidRepository extends JpaRepository<AclOid, Long>, AclOidCustomRepository {
}
