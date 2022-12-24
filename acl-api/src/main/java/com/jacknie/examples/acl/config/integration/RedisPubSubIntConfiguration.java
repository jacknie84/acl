package com.jacknie.examples.acl.config.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jacknie.examples.acl.config.security.acls.cache.AclCacheEvictionEventListener;
import com.jacknie.examples.acl.config.security.acls.domain.AclModel;
import com.jacknie.examples.acl.config.security.acls.service.OidFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.integration.redis.inbound.RedisInboundChannelAdapter;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.security.acls.model.ObjectIdentity;

import java.io.Serializable;

import static com.jacknie.examples.acl.config.integration.MessageChannelNames.PUB_ACL_CACHE_EVICTION_CHANNEL;

@Configuration
@RequiredArgsConstructor
public class RedisPubSubIntConfiguration {

    private static final String TOPIC = "acl-api::acl-cache.eviction-topic";

    private final RedisConnectionFactory redisConnectionFactory;
    private final ObjectMapper objectMapper;
    private final AclCacheEvictionEventListener evictionEventListener;
    private final OidFactory oidFactory;
    private final StringRedisTemplate redisTemplate;

    @Bean
    public IntegrationFlow subscribeAclCacheEvictionTopic() {
        RedisInboundChannelAdapter channelAdapter = new RedisInboundChannelAdapter(redisConnectionFactory);
        channelAdapter.setTopics(TOPIC);
        GenericTransformer<String, AclModel> aclModelTransformer = payload -> {
            try {
                return objectMapper.readValue(payload, AclModel.class);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(e);
            }
        };
        GenericHandler<AclModel> aclModelHandler = (aclModel, headers) -> {
            Serializable pk = aclModel.getId();
            ObjectIdentity oid = oidFactory.createObjectIdentity(aclModel.getOid());
            evictionEventListener.onEvict(pk, oid);
            return null;
        };
        return IntegrationFlows.from(channelAdapter)
            .transform(String.class, aclModelTransformer)
            .handle(AclModel.class, aclModelHandler)
            .log()
            .get();
    }

    @Bean
    public IntegrationFlow publishAclCacheEvictionTopic() {
        GenericTransformer<AclModel, String> aclModelTransformer = payload -> {
            try {
                return objectMapper.writeValueAsString(payload);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(e);
            }
        };
        GenericHandler<String> payloadHandler = (payload, headers) -> {
            redisTemplate.convertAndSend(TOPIC, payload);
            return null;
        };
        return IntegrationFlows.from(PUB_ACL_CACHE_EVICTION_CHANNEL)
            .transform(AclModel.class, aclModelTransformer)
            .handle(String.class, payloadHandler)
            .log()
            .get();
    }
}
