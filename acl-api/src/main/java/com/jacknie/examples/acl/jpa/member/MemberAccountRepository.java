package com.jacknie.examples.acl.jpa.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberAccountRepository extends JpaRepository<MemberAccount, Long> {
    Optional<MemberAccount> findByEmail(String email);
}
