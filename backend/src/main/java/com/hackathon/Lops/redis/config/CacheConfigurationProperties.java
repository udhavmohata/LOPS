package com.hackathon.Lops.redis.config;

import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties
public class CacheConfigurationProperties {

    private long defaultTimeTolive = 600000L;
    private Map<String, Long> cacheExpiry = new HashMap<>();
    private Integer poolMaxConnections = 100;
    private Integer poolMaxIdleConnections = 10;
    private Integer poolMinIdleConnections = 5;
    private String defaultTopic = "message:queue";
    private boolean isAsyncSubscriber = false;
    private Long commandExecutionTimeout = 500L;
    private Long connectionTimeout = 1000L;
    private Long maxEvictionIdleTime = GenericObjectPoolConfig.DEFAULT_MIN_EVICTABLE_IDLE_DURATION.toMillis();
}
