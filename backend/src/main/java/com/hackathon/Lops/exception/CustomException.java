package com.hackathon.Lops.exception;

public class CustomException extends Exception{
   String message;
    public CustomException(String s) {
        this.message =s;
    }
}
