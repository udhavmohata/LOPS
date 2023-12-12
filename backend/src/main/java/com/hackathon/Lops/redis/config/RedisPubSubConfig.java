package com.hackathon.Lops.redis.config;

import com.hackathon.Lops.redis.subscriber.RedisMessageSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@EnableConfigurationProperties(CacheConfigurationProperties.class)
public class RedisPubSubConfig {

    @Autowired
    @Qualifier("subscriber-redis-con")
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    PatternTopic topic(CacheConfigurationProperties cacheProperties) {
        return new PatternTopic(cacheProperties.getDefaultTopic());
    }

    @Bean
    MessageListenerAdapter defaultMessageListener() {
        return new MessageListenerAdapter(new RedisMessageSubscriber());
    }

    @Bean
    RedisMessageListenerContainer redisContainer(CacheConfigurationProperties cacheProperties) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(defaultMessageListener(), topic(cacheProperties));
        return container;
    }
}
