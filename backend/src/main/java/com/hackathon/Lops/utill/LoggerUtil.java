package com.hackathon.Lops.utill;

import com.hackathon.Lops.constant.LoggingConstants;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Optional;

public class LoggerUtil {

    private final Logger logger;

    public LoggerUtil(Logger log) {
        this.logger = log;
    }

    public static LoggerUtil getLogger(final Class<?> clazz) {
        return new LoggerUtil(LogManager.getLogger(clazz));
    }

    public void info(String data){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("info", data);
        } catch (Exception ignored) {

        }
        log(Level.INFO, jsonObject, LoggingConstants.INFO_LOG_KEY);
    }

    public void debug(String data){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("debug", data);
        } catch (Exception ignored) {

        }
        log(Level.DEBUG, jsonObject, LoggingConstants.DEBUG_LOG_KEY);
    }

    public void error(String data){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("error", data);
        } catch (Exception ignored) {

        }
        log(Level.ERROR, jsonObject, LoggingConstants.ERROR_LOG_KEY);
    }

    public void stackTrace(Exception e){
        StringWriter stack = new StringWriter();
        e.printStackTrace(new PrintWriter(stack));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("trace", stack.toString());
        } catch (Exception ignored) {

        }
        log(Level.ERROR, jsonObject, LoggingConstants.ERROR_LOG_KEY);
    }



    private <T> void log(Level logLevel, T data, String logLabel) {
        JSONObject logObject = new JSONObject(new HashMap<String, T>() {{
            put(Optional.ofNullable(logLabel).orElse("info"), data);}});
        logger.log(logLevel, logObject);
    }

}
