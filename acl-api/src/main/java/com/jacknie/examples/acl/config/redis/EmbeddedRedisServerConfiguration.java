package com.jacknie.examples.acl.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
@RequiredArgsConstructor
public class EmbeddedRedisServerConfiguration implements ApplicationListener<ApplicationEvent> {

    private final RedisProperties redisProperties;

    private RedisServer redisServer;

    @PostConstruct
    public void postConstruct() {
        if (redisServer == null) {
            this.redisServer = new RedisServer(redisProperties.getPort());
        }
        if (!redisServer.isActive()) {
            redisServer.start();
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("onApplicationEvent: " + event);
    }

    @PreDestroy
    public void preDestroy() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}
