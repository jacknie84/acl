package com.jacknie.examples.acl.web.member.account;

import com.jacknie.examples.acl.jpa.member.MemberRole;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class PutMemberAccountDto {

    @Length(min = 6, max = 15)
    private String password;

    @NotEmpty
    private Set<@NotNull MemberRole> roles;
}
