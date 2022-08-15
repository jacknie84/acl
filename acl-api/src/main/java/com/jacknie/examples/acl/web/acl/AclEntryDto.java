package com.jacknie.examples.acl.web.acl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AclEntryDto {

    private Long id;
    private boolean granting;
    private boolean read;
    private boolean write;
    private boolean remove;
    private boolean create;
    private boolean admin;
    private Long aclId;
    private AclSidDto sid;
}
