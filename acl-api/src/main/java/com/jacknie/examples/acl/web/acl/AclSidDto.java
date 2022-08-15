package com.jacknie.examples.acl.web.acl;

import com.jacknie.examples.acl.jpa.acl.sid.SidType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AclSidDto {

    private SidType type;
    private String sid;
}
