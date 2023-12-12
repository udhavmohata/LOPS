package com.hackathon.Lops.redis.utill;

import org.springframework.data.redis.connection.Message;

public interface RedisMessageHandler {
    public void processMessage(Message message, byte[] pattern);
    public void processMessageAsync(Message message, byte[] pattern);
}
