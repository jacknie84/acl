package com.jacknie.examples.acl.web.acl;

import com.jacknie.examples.acl.config.security.acl.AclIdentifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AclDto implements AclIdentifiable {

    private Long id;
    private boolean entriesInheriting;
    private String domainCode;
    private AclSidDto owner;
    private Long parentId;
}
