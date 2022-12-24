package com.jacknie.examples.acl.config.security.acls.hierarchy.impl;

import com.jacknie.examples.acl.config.security.acls.hierarchy.AclHierarchyKeeper;
import com.jacknie.examples.acl.config.security.acls.hierarchy.AclHierarchyKeeperComponent;
import com.jacknie.examples.acl.config.security.acls.service.AclConstantService;
import com.jacknie.examples.acl.config.security.acls.service.AclServiceTemplate;
import com.jacknie.examples.acl.config.security.acls.service.SidFactory;
import com.jacknie.examples.acl.jpa.acl.constant.AclConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class AclHierarchyKeeperImpl implements AclHierarchyKeeper {

    private final SidFactory sidFactory;
    private final AclServiceTemplate aclServiceTemplate;
    private final AclConstantService constantService;

    private List<AclHierarchyKeeperComponent> components;

    @PostConstruct
    public void postConstruct() {
        AclHierarchyKeeperComponent root = RootAclHierarchyKeeperComponent.builder()
            .aclServiceTemplate(aclServiceTemplate)
            .sidFactory(sidFactory)
            .build();
        AclHierarchyKeeperComponent domain = DomainAclHierarchyKeeperComponent.builder()
            .aclServiceTemplate(aclServiceTemplate)
            .sidFactory(sidFactory)
            .constantService(constantService)
            .root(root)
            .build();
        this.components = List.of(root, domain);
    }

    @Override
    public void initialize(AclConstant constant) {
        AclHierarchyKeeperComponent keeper = components.stream()
            .filter(component -> component.isSupport(constant))
            .findFirst()
            .orElseThrow();
        keeper.keepAces(constant);
    }
}
