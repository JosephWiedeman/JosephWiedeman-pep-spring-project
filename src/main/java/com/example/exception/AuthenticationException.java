package com.example.exception;

public class AuthenticationException extends RuntimeException{
    private String message;
    
    public AuthenticationException() {}
    
    public AuthenticationException(String msg) {
        super(msg);
        this.message = msg;
    }
}

