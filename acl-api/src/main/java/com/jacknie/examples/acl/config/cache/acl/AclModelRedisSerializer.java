package com.jacknie.examples.acl.config.cache.acl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jacknie.examples.acl.config.security.acls.domain.AclModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.SerializationUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
public class AclModelRedisSerializer implements RedisSerializer<AclModel> {

    private final ObjectMapper objectMapper;

    @Nullable
    @Override
    public byte[] serialize(@Nullable AclModel aclModel) throws SerializationException {
        if (aclModel == null) {
            return new byte[0];
        }
        try {
            return objectMapper.writeValueAsBytes(aclModel);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Could not write JSON: " + e.getMessage(), e);
        }
    }

    @Nullable
    @Override
    public AclModel deserialize(@Nullable byte[] bytes) throws SerializationException {
        if (ObjectUtils.isEmpty(bytes)) {
            return null;
        }
        try {
            return objectMapper.readValue(bytes, AclModel.class);
        } catch (Exception e) {
            throw new SerializationException("Could not read JSON: " + e.getMessage(), e);
        }
    }
}
