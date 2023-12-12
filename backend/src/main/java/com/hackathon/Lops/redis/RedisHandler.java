package com.hackathon.Lops.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.hackathon.Lops.enums.RedisOps;
import com.hackathon.Lops.exception.CustomException;
import com.hackathon.Lops.utill.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Component
public class RedisHandler {

    private final LoggerUtil loggerUtil = LoggerUtil.getLogger(RedisHandler.class);



    @Autowired
    private RedisOperations<String, String> stringRedisOperations;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    public <T> T redisOps(RedisOps operation, Object... args) throws Exception {

        try {
            validateArguments(operation, args);
        } catch (CustomException e) {
            loggerUtil.error(e.getMessage());
            return null;
        }

        switch (operation) {

            case SET_VALUE -> {
                stringRedisOperations.opsForValue().set(asString(args[0]), asString((args[1])));
                return (T) Boolean.TRUE;
            }

            case SET_VALUE_WITH_TTL -> {
                stringRedisOperations.opsForValue().set(asString(args[0]), asString(args[1]), (Long) args[2], (TimeUnit) args[3]);
                return (T) Boolean.TRUE;
            }

            case SET_VALUE_IF_ABSENT -> {
                return (T) stringRedisOperations.opsForValue().setIfAbsent(asString(args[0]), asString(args[1]));
            }

            case SET_VALUE_IF_ABSENT_WITH_TTL -> {
                return (T) stringRedisOperations.opsForValue().setIfAbsent(asString(args[0]), asString(args[1]), (Long) args[2], (TimeUnit) args[3]);
            }

            case GET -> {
                return (T) stringRedisOperations.opsForValue().get(asString(args[0]));
            }

            case MULTI_GET -> {
                return (T) stringRedisOperations.opsForValue().multiGet((Collection<String>) args[0]);
            }

            case DELETE -> {
                return (T) stringRedisOperations.delete(asString(args[0]));
            }

            case DELETE_ALL -> {
                return (T) stringRedisOperations.delete((Collection<String>) args[0]);
            }

            case HAS_KEY -> {
                return (T) stringRedisOperations.hasKey(asString(args[0]));
            }

            case KEYS -> {
                return (T) stringRedisOperations.keys(asString(args[0]));
            }

            default -> {
                return null;
            }
        }
    }


    private void validateArguments(RedisOps operation, Object... args) throws CustomException {

        switch (operation) {
            case SET_VALUE, SET_VALUE_IF_ABSENT -> {
                if (args == null || args.length != 2) {
                    throw new CustomException("[REDIS-OPS] : Invalid Arguments for " + operation);
                }
            }
            case SET_VALUE_WITH_TTL, SET_VALUE_IF_ABSENT_WITH_TTL -> {
                if (args == null || args.length != 4 || !(args[2] instanceof Long || args[2] instanceof Integer) || !(args[3] instanceof TimeUnit)) {
                    throw new CustomException("[REDIS-OPS] : Invalid Arguments for " + operation);
                }
            }
            case GET, DELETE, HAS_KEY, KEYS -> {
                if (args == null || args.length != 1) {
                    throw new CustomException("[REDIS-OPS] : Invalid Arguments for " + operation);
                }
            }
            case MULTI_GET, DELETE_ALL -> {
                if(args == null || args.length != 1 || !(args[0] instanceof Collection<?>)){
                    throw new CustomException("[REDIS-OPS] : Invalid Arguments for " + operation);
                }
            }
            default -> {
            }
        }
    }


    private String asString(Object args) throws JsonProcessingException {
        if(args instanceof String) {
            return (String) args;
        }
        return objectMapper.writeValueAsString(args);
    }

}
