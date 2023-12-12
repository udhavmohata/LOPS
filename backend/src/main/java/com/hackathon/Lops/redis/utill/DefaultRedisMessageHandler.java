package com.hackathon.Lops.redis.utill;

import com.hackathon.Lops.utill.LoggerUtil;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class DefaultRedisMessageHandler implements  RedisMessageHandler{


    LoggerUtil log = LoggerUtil.getLogger(DefaultRedisMessageHandler.class);

    @Override
    public void processMessage(Message message, byte[] pattern) {
        log.info("Default redis message handler, listening to messages from default topic");
    }

    @Override
    public void processMessageAsync(Message message, byte[] pattern) {
        log.info("Default redis message handler, listening to messages from default topic");
    }
}