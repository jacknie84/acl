package com.jacknie.examples.acl.config.security.acl;

import com.jacknie.examples.acl.config.security.acl.domain.AclAdmin;
import com.jacknie.examples.acl.config.security.acl.domain.AclDomainConfig;
import com.jacknie.examples.acl.config.security.acl.support.*;
import com.jacknie.examples.acl.jpa.acl.entry.AclEntryRepository;
import com.jacknie.examples.acl.jpa.acl.oid.AclObjectIdentityRepository;
import com.jacknie.examples.acl.jpa.acl.sid.AclSidRepository;
import com.jacknie.examples.acl.jpa.acl.type.AclClassRepository;
import com.jacknie.examples.acl.jpa.member.MemberRole;
import com.jacknie.examples.acl.web.building.facility.BuildingFacilityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;

import static com.jacknie.examples.acl.config.security.acl.domain.AclDomainCodes.ADMIN;
import static com.jacknie.examples.acl.config.security.acl.domain.AclDomainCodes.BUILDING_FACILITY;

@Configuration
@RequiredArgsConstructor
public class AclConfiguration {

    private final AclObjectIdentityRepository oidRepository;
    private final AclSidRepository sidRepository;
    private final AclClassRepository classRepository;
    private final AclEntryRepository entryRepository;

    @Bean
    public PermissionEvaluator permissionEvaluator() {
        ObjectIdentityConverter oidConverter = new ObjectIdentityConverter(oidFactory());
        AclPermissionEvaluator evaluator = new AclPermissionEvaluator(aclService());
        evaluator.setObjectIdentityGenerator(oidConverter);
        evaluator.setObjectIdentityRetrievalStrategy(oidConverter);
        return evaluator;
    }

    @Bean
    public ObjectIdentityFactory oidFactory() {
        return new DefaultObjectIdentityFactory(aclDomainConfig());
    }

    @Bean
    public AclDomainConfig aclDomainConfig() {
        return AclDomainConfig.builder()
            .addDomain(ADMIN, AclAdmin.class)
            .addDomain(BUILDING_FACILITY, BuildingFacilityDto.class)
            .addIdConverter(Long::parseLong, Long.class)
            .build();
    }

    @Bean
    public MutableAclService aclService() {
        AuditLogger auditLogger = new ConsoleAuditLogger();
        Cache cache = new ConcurrentMapCache("aclCache");
        PermissionGrantingStrategy grantingStrategy = new CumulativePermissionGrantingStrategy(auditLogger);
        AclAuthorizationStrategy aclAuthorizationStrategy = new AclAuthorizationStrategyImpl(MemberRole.ROLE_ADMIN);
        AclCache aclCache = new SpringCacheBasedAclCache(cache, grantingStrategy, aclAuthorizationStrategy);
        AclOperationsImpl operations = AclOperationsImpl.builder()
            .oidRepository(oidRepository)
            .sidRepository(sidRepository)
            .classRepository(classRepository)
            .entryRepository(entryRepository)
            .idConversionService(aclDomainConfig().getIdConversionService())
            .oidFactory(oidFactory())
            .build();
        LookupStrategy lookupStrategy = LookupStrategyImpl.builder()
            .aclCache(aclCache)
            .lookupOperations(operations)
            .aclAuthorizationStrategy(aclAuthorizationStrategy)
            .grantingStrategy(grantingStrategy)
            .build();
        return MutableAclServiceImpl.builder()
            .aclOperations(operations)
            .aclCache(aclCache)
            .lookupStrategy(lookupStrategy)
            .build();
    }
}
