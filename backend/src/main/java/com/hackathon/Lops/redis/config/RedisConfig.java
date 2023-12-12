package com.hackathon.Lops.redis.config;

import com.hackathon.Lops.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.Objects;

@Configuration
public class RedisConfig {

    @Autowired
    private AppProperties appProperties;

    @Bean
    @Primary
    public RedisStandaloneConfiguration redisConfiguration() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(appProperties.getRedisHost());
        config.setPort(appProperties.getRedisPort());
        //config.setUsername(appProperties.getRedisUserName());
        //config.setPassword(RedisPassword.of(appProperties.getRedisPassword()));
        return config;
    }

    @Bean(name = "default-redis-con")
   // @Primary
    public JedisConnectionFactory jedisConnectionFactory(RedisStandaloneConfiguration config) {
        JedisConnectionFactory jedisConnectionFactory;
        if(Boolean.FALSE.equals(appProperties.getRedisUseSsl())) {
            jedisConnectionFactory = new JedisConnectionFactory(config);
        } else {
            JedisClientConfiguration clientConfiguration = JedisClientConfiguration.builder().useSsl().build();
            jedisConnectionFactory = new JedisConnectionFactory(config, clientConfiguration);
        }
        Objects.requireNonNull(jedisConnectionFactory.getPoolConfig()).setMaxTotal(50);
        jedisConnectionFactory.getPoolConfig().setMaxIdle(50);
        return jedisConnectionFactory;
    }

    @Bean
    @Primary
    public RedisOperations<String, String> redisStringRedisTemplate(JedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    @Primary
    public RedisOperations<String, Object> redisTemplate(JedisConnectionFactory connectionFactory) {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setValueSerializer(serializer);
        return template;
    }

    @Bean(name = "subscriber-redis-con")
   @Primary
    JedisConnectionFactory jedisSubscriberConnectionFactory() {

//        if (appProperties.getSentinel() != null && appProperties.getSentinel()) {
//
//            RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
//            //redisSentinelConfiguration.setDatabase(appProperties.getRedisDatabase());
//            //redisSentinelConfiguration.setUsername(appProperties.getRedisUserName());
//            //redisSentinelConfiguration.setPassword(RedisPassword.of(appProperties.getRedisPassword()));
//           // redisSentinelConfiguration.master(redisProperties.getSentinel().getMaster());
//            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisSentinelConfiguration);
//            return jedisConnectionFactory;
//        } else {
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
            //redisStandaloneConfiguration.setDatabase(appProperties.getRedisDatabase());
            redisStandaloneConfiguration.setHostName(appProperties.getRedisHost());
            redisStandaloneConfiguration.setPort(appProperties.getRedisPort());
            //  redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
            //redisStandaloneConfiguration.setUsername(redisProperties.getUsername());
            return new JedisConnectionFactory(redisStandaloneConfiguration);
        //}
    }

}
