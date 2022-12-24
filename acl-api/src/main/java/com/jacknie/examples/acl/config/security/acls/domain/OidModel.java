package com.jacknie.examples.acl.config.security.acls.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.util.Assert;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OidModel {

    private String type;

    private String identifier;

    public static OidModel from(ObjectIdentity oid) {
        Assert.notNull(oid, "oid cannot be null");
        Assert.hasText(oid.getType(), "oid.type cannot be blank");
        Assert.notNull(oid.getIdentifier(), "oid.identifier cannot be null");
        return new OidModel(oid.getType(), oid.getIdentifier().toString());
    }
}
