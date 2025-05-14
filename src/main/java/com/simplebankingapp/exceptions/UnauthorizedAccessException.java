package com.simplebankingapp.exceptions;

public class UnauthorizedAccessException extends RuntimeException{
    private String msg;
    public UnauthorizedAccessException(String message){
        this.msg = message;
    }

    public String getMsg() {
        return msg;
    }
}
