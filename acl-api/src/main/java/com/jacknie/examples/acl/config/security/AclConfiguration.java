package com.jacknie.examples.acl.config.security;

import com.jacknie.examples.acl.config.cache.CacheNames;
import com.jacknie.examples.acl.config.integration.RedisAclCacheMessagingGateway;
import com.jacknie.examples.acl.config.security.acls.AclPermissionEvaluatorImpl;
import com.jacknie.examples.acl.config.security.acls.CustomAclPermissionEvaluator;
import com.jacknie.examples.acl.config.security.acls.cache.CompositeAclCache;
import com.jacknie.examples.acl.config.security.acls.cache.MemoryAclCache;
import com.jacknie.examples.acl.config.security.acls.cache.RedisAclCache;
import com.jacknie.examples.acl.config.security.acls.domain.AclDomains;
import com.jacknie.examples.acl.config.security.acls.domain.AclIdentifiable;
import com.jacknie.examples.acl.config.security.acls.hierarchy.AclHierarchyKeeper;
import com.jacknie.examples.acl.config.security.acls.hierarchy.impl.AclHierarchyKeeperImpl;
import com.jacknie.examples.acl.config.security.acls.service.*;
import com.jacknie.examples.acl.config.security.acls.service.impl.*;
import com.jacknie.examples.acl.jpa.acl.constant.AclConstantRepository;
import com.jacknie.examples.acl.jpa.acl.domain.AclDomainRepository;
import com.jacknie.examples.acl.jpa.acl.entry.AclEntryRepository;
import com.jacknie.examples.acl.jpa.acl.oid.AclOidRepository;
import com.jacknie.examples.acl.jpa.acl.sid.AclSidRepository;
import com.jacknie.examples.acl.jpa.member.MemberRole;
import com.jacknie.experimental.acl.support.CumulativePermissionGrantingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.util.NumberUtils;

@Configuration
@RequiredArgsConstructor
public class AclConfiguration {

    private final AclConstantRepository constantRepository;
    private final AclOidRepository oidRepository;
    private final AclSidRepository sidRepository;
    private final AclDomainRepository domainRepository;
    private final AclEntryRepository entryRepository;
    private final CacheManager cacheManager;
    private final RedisAclCacheMessagingGateway messagingGateway;

    @Bean
    public CustomAclPermissionEvaluator permissionEvaluator() {
        AclPermissionEvaluatorImpl permissionEvaluator = new AclPermissionEvaluatorImpl(aclService());
        permissionEvaluator.setObjectIdentityGenerator(oidFactory());
        permissionEvaluator.setObjectIdentityRetrievalStrategy(oidFactory());
        permissionEvaluator.setSidRetrievalStrategy(sidFactory());
        permissionEvaluator.setPermissionFactory(permissionFactory());
        permissionEvaluator.setConstantService(constantService());
        permissionEvaluator.setHierarchyKeeper(hierarchyKeeper());
        return permissionEvaluator;
    }

    @Bean
    public MutableAclService aclService() {
        LookupStrategy lookupStrategy = LookupStrategyImpl.builder()
            .aclCache(aclCache())
            .permissionFactory(permissionFactory())
            .authorizationStrategy(authorizationStrategy())
            .grantingStrategy(grantingStrategy())
            .lookupOperations(aclOperations())
            .build();
        return MutableAclServiceImpl.builder()
            .aclOperations(aclOperations())
            .aclCache(aclCache())
            .lookupStrategy(lookupStrategy)
            .build();
    }

    @Bean
    public CompositeAclCache aclCache() {
        Cache mapCache = new ConcurrentMapCache(CacheNames.ACL_CACHE_MEMORY);
        AclCache memoryCache = new MemoryAclCache(mapCache);
        AclCache redisCache = RedisAclCache.builder()
            .cache(cacheManager.getCache(CacheNames.ACL_CACHE_REDIS))
            .oidFactory(oidFactory())
            .sidFactory(sidFactory())
            .permissionFactory(permissionFactory())
            .authorizationStrategy(authorizationStrategy())
            .grantingStrategy(grantingStrategy())
            .messagingGateway(messagingGateway)
            .build();
        return new CompositeAclCache(memoryCache, redisCache);
    }

    @Bean
    public AclAuthorizationStrategy authorizationStrategy() {
        AclAuthorizationStrategyImpl authorizationStrategy = new AclAuthorizationStrategyImpl(MemberRole.ROLE_ADMIN);
        authorizationStrategy.setSidRetrievalStrategy(sidFactory());
        return authorizationStrategy;
    }

    @Bean
    public PermissionGrantingStrategy grantingStrategy() {
        AuditLogger auditLogger = new ConsoleAuditLogger();
        return new CumulativePermissionGrantingStrategy(auditLogger);
    }

    @Bean
    public AclOperations aclOperations() {
        return AclOperationsImpl.builder()
            .oidRepository(oidRepository)
            .sidRepository(sidRepository)
            .domainRepository(domainRepository)
            .entryRepository(entryRepository)
            .idConverterRepository(idConverterRepository())
            .build();
    }

    @Bean
    public OidFactory oidFactory() {
        return new OidFactoryImpl(idConverterRepository(), domainCodeRepository());
    }

    @Bean
    public AclDomainIdConverterRepository idConverterRepository() {
        return AclDomainIdConverterRepositoryImpl.builder()
            .map("toString", String.class, identifier -> identifier)
            .map("toLong", Long.class, identifier -> NumberUtils.parseNumber(identifier, Long.class))
            .build();
    }

    @Bean
    public AclDomainCodeRepository domainCodeRepository() {
        AclDomainCodeRepositoryImpl.Builder builder = AclDomainCodeRepositoryImpl.builder();
        for (AclDomains domains : AclDomains.values()) {
            for (Class<? extends AclIdentifiable> type : domains.getTypes()) {
                builder.map(type, domains.getCode());
            }
        }
        return builder.build();
    }

    @Bean
    public SidFactory sidFactory() {
        return new SidFactoryImpl();
    }

    @Bean
    public PermissionFactory permissionFactory() {
        return new AbbreviationPermissionFactory();
    }

    @Bean
    public AclConstantService constantService() {
        return AclConstantServiceImpl.builder()
            .constantRepository(constantRepository)
            .build();
    }

    @Bean
    public AclHierarchyKeeper hierarchyKeeper() {
        return AclHierarchyKeeperImpl.builder()
            .sidFactory(sidFactory())
            .aclServiceTemplate(aclServiceTemplate())
            .build();
    }

    @Bean
    public AclServiceTemplate aclServiceTemplate() {
        return AclServiceTemplateImpl.builder()
            .build();
    }
}
