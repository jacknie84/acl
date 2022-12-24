package com.jacknie.examples.acl.config.integration;

import com.jacknie.examples.acl.config.security.acls.domain.AclModel;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface RedisAclCacheMessagingGateway {

    @Gateway(requestChannel = MessageChannelNames.PUB_ACL_CACHE_EVICTION_CHANNEL)
    void publishAclCacheEviction(AclModel aclModel);
}
