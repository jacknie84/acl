package com.jacknie.examples.acl.web.member;

import com.jacknie.examples.acl.jpa.member.MemberRole;
import lombok.Data;

@Data
public class MemberAccountsFilterDto {
    private String search;
}
