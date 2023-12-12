package com.hackathon.Lops.services;


import com.hackathon.Lops.constant.TriggerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LockingService implements ILockingService {


        @Autowired
        StringRedisTemplate stringRedisTemplate;

        @Override
        public Boolean acquireLock(String lockId, Long lockUntil, TimeUnit timeUnit) {
            return stringRedisTemplate.opsForValue()
                    .setIfAbsent(lockId, TriggerConstants.TRIGGER_KEY_PREFIX.concat(lockId), lockUntil,
                            timeUnit);
        }

        @Override
        public Boolean releaseLock(String lockId) {
            return stringRedisTemplate.expire(lockId, 0L, TimeUnit.SECONDS);
        }


    }

