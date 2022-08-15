package com.jacknie.examples.acl.web.acl;

import com.jacknie.examples.acl.jpa.acl.sid.SidType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;

@Data
public class AclFilterDto {

    private Set<@NotBlank String> domainCodes;

    private Set<@NotNull @Valid Sid> ownerSids;

    private Set<@NotNull @Valid Sid> entrySids;

    private Set<@NotNull @Positive Long> accountIds;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Sid {

        @NotNull
        private SidType type;

        @NotBlank
        private String sid;
    }
}
