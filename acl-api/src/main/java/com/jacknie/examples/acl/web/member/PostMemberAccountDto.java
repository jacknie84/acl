package com.jacknie.examples.acl.web.member;

import com.jacknie.examples.acl.jpa.member.MemberRole;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class PostMemberAccountDto {

    @Email
    @NotBlank
    private String email;

    @Length(min = 6, max = 15)
    @NotBlank
    private String password;

    @NotEmpty
    private Set<@NotNull MemberRole> roles;
}
