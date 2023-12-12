package com.hackathon.Lops.listener;

import com.hackathon.Lops.constant.TriggerConstants;
import com.hackathon.Lops.executor.TriggerExecutor;

import com.hackathon.Lops.redis.RedisHandler;
import com.hackathon.Lops.services.ILockingService;
import com.hackathon.Lops.services.RedisEventListenerService;
import com.hackathon.Lops.utill.LoggerUtil;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.lang.Nullable;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class RedisEventListener extends KeyExpirationEventMessageListener {

    ThreadPoolExecutor executor;


    @PostConstruct
    public void createThreadPool() {
        executor = new ThreadPoolExecutor(0, 2, 60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }
    @Autowired
    private TriggerExecutor triggerExecutor;

    @Autowired
    private ILockingService lockingService;

    @Autowired
    private RedisEventListenerService redisEventListenerService;

    private LoggerUtil logger = LoggerUtil.getLogger(RedisEventListener .class);


    public RedisEventListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }


    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {
        logger.info("Redis key expired event triggered : "+message );
        if (message.toString().startsWith(TriggerConstants.TRIGGER_KEY_PREFIX) || 1==1) {
            try {
                redisEventListenerService.processRedisKeyExpireEvent(message.toString());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Error : "+e.getMessage());
            }

        }
    }

    private UUID getTriggerIdFromKey(String key) {
        return UUID.fromString(key.substring(TriggerConstants.TRIGGER_KEY_PREFIX.length()));
    }

    private String getLockingKey(String message) {
        return TriggerConstants.LOCK_PREFIX.concat(message);
    }
}