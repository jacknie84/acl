package com.jacknie.examples.acl.config.security.acls.service.impl;

import com.jacknie.examples.acl.config.security.acls.domain.SidModel;
import com.jacknie.examples.acl.config.security.acls.domain.SidType;
import com.jacknie.examples.acl.config.security.acls.service.SidFactory;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

public class SidFactoryImpl extends SidRetrievalStrategyImpl implements SidFactory {

    @Override
    public Sid createSid(SidModel sidModel) {
        Assert.notNull(sidModel, "sidModel cannot be null");
        Assert.notNull(sidModel.getType(), "sidModel.type cannot be null");
        Assert.hasText(sidModel.getValue(), "sidModel.value cannot be blank");
        return sidModel.getType().createSid(sidModel.getValue());
    }

    @Override
    public Sid createSid(GrantedAuthority grantedAuthority) {
        Assert.notNull(grantedAuthority, "grantedAuthority cannot be null");
        Assert.hasText(grantedAuthority.getAuthority(), "grantedAuthority.authority cannot be blank");
        return SidType.GRANTED_AUTHORITY.createSid(grantedAuthority.getAuthority());
    }
}
