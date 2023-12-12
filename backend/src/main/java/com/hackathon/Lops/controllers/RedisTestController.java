package com.hackathon.Lops.controllers;

import com.hackathon.Lops.beans.ReasonCode;
import com.hackathon.Lops.enums.RedisOps;
import com.hackathon.Lops.redis.RedisHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redis")
public class RedisTestController {

    @Autowired
    private RedisHandler redisHandler;

    @PostMapping(value = "/set")
    public Boolean setValue(@RequestParam(name = "key") String  key,@RequestParam(name ="value")String value,@RequestParam("ttl") Long ttl) throws Exception {
        return redisHandler.redisOps(RedisOps.SET_VALUE_WITH_TTL,key,value,ttl, TimeUnit.MILLISECONDS);
    }

    @DeleteMapping
    public Boolean deleteKey(@RequestParam(name = "key") String  key) throws Exception {
        return redisHandler.redisOps(RedisOps.DELETE,key);
    }

    @GetMapping
    public String getData(@RequestParam(name = "key") String  key) throws Exception {
        return redisHandler.redisOps(RedisOps.GET,key);
    }
}
