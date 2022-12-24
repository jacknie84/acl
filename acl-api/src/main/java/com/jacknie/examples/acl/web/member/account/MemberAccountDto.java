package com.jacknie.examples.acl.web.member.account;

import com.jacknie.examples.acl.config.security.acls.domain.AclIdentifiable;
import com.jacknie.examples.acl.jpa.member.MemberAccount;
import com.jacknie.examples.acl.jpa.member.MemberRole;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
public class MemberAccountDto implements AclIdentifiable {
    private Long id;
    private String email;
    private Set<MemberRole> roles;
    private Instant lastModifiedDate;

    public static MemberAccountDto from(MemberAccount entity) {
        return builder()
            .id(entity.getId())
            .email(entity.getEmail())
            .roles(entity.getRoles())
            .lastModifiedDate(entity.getLastModifiedDate())
            .build();
    }
}
