package com.jacknie.examples.acl.config.security.acls.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Sid;
import org.springframework.util.Assert;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SidModel {

    private SidType type;

    private String value;

    public static SidModel from(Sid sid) {
        Assert.notNull(sid, "sid cannot be null");
        if (sid instanceof PrincipalSid principalSid) {
            Assert.notNull(principalSid.getPrincipal(), "sid.principal cannot be blank");
            return new SidModel(SidType.PRINCIPAL, principalSid.getPrincipal());
        }
        if (sid instanceof GrantedAuthoritySid grantedAuthoritySid) {
            Assert.notNull(grantedAuthoritySid.getGrantedAuthority(), "sid.grantedAuthority cannot be blank");
            return new SidModel(SidType.GRANTED_AUTHORITY, grantedAuthoritySid.getGrantedAuthority());
        }
        throw new IllegalStateException("unsupported sid type: " + sid.getClass());
    }
}
