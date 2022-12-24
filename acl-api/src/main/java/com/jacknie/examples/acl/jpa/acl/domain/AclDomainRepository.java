package com.jacknie.examples.acl.jpa.acl.type;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AclClassRepository extends JpaRepository<AclClass, Long>, AclClassCustomRepository {

    boolean existsByClassName(String className);
}
