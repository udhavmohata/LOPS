package com.hackathon.Lops.redis.utill;

import com.hackathon.Lops.utill.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class AddRedisSubscriber {

    LoggerUtil log = LoggerUtil.getLogger(AddRedisSubscriber.class);

    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;

    public void addListner(MessageListener messageListener, String topic) {
        try {
            redisMessageListenerContainer.addMessageListener(messageListener, new ChannelTopic(topic));
        } catch (Exception ex) {
            log.error("Error while registering redis listner : "+ ex.getMessage());
        }

    }
}
