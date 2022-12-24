package com.jacknie.examples.acl.config.security.acls.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AceModel {

    @Nullable
    private Long id;

    private long aclId;

    private SidModel sid;

    private int mask;

    private boolean granting;

    private boolean auditSuccess;

    private boolean auditFailure;
}
