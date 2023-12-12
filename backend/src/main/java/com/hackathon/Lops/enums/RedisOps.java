package com.hackathon.Lops.enums;

public enum RedisOps {
    SET_VALUE("set_value"),
    SET_VALUE_WITH_TTL("set_value_ttl"),
    SET_VALUE_IF_ABSENT("set_if_absent"),
    SET_VALUE_IF_ABSENT_WITH_TTL("set_if_absent_with_ttl"),

    GET("get"),
    MULTI_GET("multi_get"),

    DELETE("delete"),
    DELETE_ALL("delete_all"),

    HAS_KEY("has_key"),
    KEYS("keys");

    private final String operation;

    RedisOps(String operation) {
        this.operation = operation;
    }

}
