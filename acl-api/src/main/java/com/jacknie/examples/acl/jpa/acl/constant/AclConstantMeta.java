package com.jacknie.examples.acl.jpa.acl.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AclConstantMeta {

    @Enumerated(EnumType.STRING)
    private AclConstantType type;

    private String code;
}
