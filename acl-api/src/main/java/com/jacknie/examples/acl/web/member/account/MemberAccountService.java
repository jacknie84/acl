package com.jacknie.examples.acl.web.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberAccountService {
    Page<MemberAccountDto> getMemberAccountsPage(MemberAccountsFilterDto dto, Pageable pageable);

    long createMemberAccount(PostMemberAccountDto dto);

    void updateMemberAccount(long id, PutMemberAccountDto dto);

    Optional<MemberAccountDto> findMemberAccount(long id);

    void deleteMemberAccount(long id);
}
