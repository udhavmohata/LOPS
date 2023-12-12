package com.hackathon.Lops.redis.subscriber;

import com.hackathon.Lops.redis.config.CacheConfigurationProperties;
import com.hackathon.Lops.redis.utill.RedisMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RedisMessageSubscriber implements MessageListener {

    @Autowired
    private RedisMessageHandler redisMessageHandler;

    @Autowired
    private CacheConfigurationProperties cacheProperties;

    public static List<String> messageList = new ArrayList<String>();

    public void onMessage(Message message, byte[] pattern) {
        messageList.add(message.toString());
        log.info("Message received: {}", message.toString());
        if(cacheProperties.isAsyncSubscriber()) {
            redisMessageHandler.processMessageAsync(message, pattern);
        } else {
            redisMessageHandler.processMessage(message, pattern);
        }
    }
}