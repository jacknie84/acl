package com.jacknie.examples.acl.config.security.acls.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AclModel {

    private long id;

    @Nullable
    private Long parentId;

    private SidModel owner;

    private OidModel oid;

    private boolean entriesInheriting;

    @Nullable
    private List<AceModel> entries;
}
