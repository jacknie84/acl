package com.jacknie.examples.acl.config.security.acls.service.impl;

import com.jacknie.examples.acl.config.security.acls.service.AclConstantService;
import com.jacknie.examples.acl.jpa.acl.constant.AclConstant;
import com.jacknie.examples.acl.jpa.acl.constant.AclConstantMeta;
import com.jacknie.examples.acl.jpa.acl.constant.AclConstantRepository;
import com.jacknie.examples.acl.jpa.acl.constant.AclConstantType;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@Builder
public class AclConstantServiceImpl implements AclConstantService {

    private final AclConstantRepository constantRepository;

    @Override
    public AclConstant getOrCreateDomain(String domainCode) {
        Assert.hasText(domainCode, "domainCode cannot be blank");
        AclConstantMeta meta = new AclConstantMeta(AclConstantType.DOMAIN, domainCode);
        return constantRepository.findOne(meta).orElseGet(() -> save(meta));
    }

    @Override
    public AclConstant getOrCreateRoot(String rootCode) {
        Assert.hasText(rootCode, "rootCode cannot be blank");
        AclConstantMeta meta = new AclConstantMeta(AclConstantType.ROOT, rootCode);
        return constantRepository.findOne(meta).orElseGet(() -> save(meta));
    }

    private AclConstant save(AclConstantMeta meta) {
        AclConstant entity = AclConstant.builder().meta(meta).build();
        return constantRepository.save(entity);
    }
}
