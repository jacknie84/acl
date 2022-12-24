package com.jacknie.examples.acl.config.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jacknie.examples.acl.config.cache.acl.AclModelRedisSerializer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.lang.Nullable;
import org.springframework.security.acls.model.ObjectIdentity;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class CacheConfiguration {

    private final ObjectMapper objectMapper;

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> {
            RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new AclModelRedisSerializer(objectMapper)));
            configuration.addCacheKeyConverter(new AclModelKeyConverter(objectMapper));
            builder.withCacheConfiguration(CacheNames.ACL_CACHE_REDIS, configuration);
        };
    }

    @RequiredArgsConstructor
    public static class AclModelKeyConverter implements Converter<ObjectIdentity, String> {

        private final ObjectMapper objectMapper;

        @SneakyThrows
        @Nullable
        @Override
        public String convert(ObjectIdentity oid) {
            return objectMapper.writeValueAsString(oid);
        }
    }
}
