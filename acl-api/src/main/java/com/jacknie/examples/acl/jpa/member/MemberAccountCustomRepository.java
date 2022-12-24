package com.jacknie.examples.acl.jpa.member;

import com.jacknie.examples.acl.web.member.account.MemberAccountsFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberAccountCustomRepository {
    Page<MemberAccount> findAll(MemberAccountsFilterDto dto, Pageable pageable);
}
