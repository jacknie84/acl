package com.jacknie.examples.acl.jpa.acl.constant;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AclConstantRepository extends JpaRepository<AclConstant, Long>, AclConstantCustomRepository {
}
