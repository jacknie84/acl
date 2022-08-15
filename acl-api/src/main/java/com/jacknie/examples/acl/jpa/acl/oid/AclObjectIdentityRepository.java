package com.jacknie.examples.acl.jpa.acl.oid;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AclObjectIdentityRepository extends JpaRepository<AclObjectIdentity, Long>, AclObjectIdentityCustomRepository {

    boolean existsByObjectIdIdentity(String objectIdIdentity);
}
