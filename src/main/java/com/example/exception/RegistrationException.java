package com.example.exception;

public class RegistrationException extends RuntimeException{
    private String message;

    public RegistrationException() {}

    public RegistrationException(String msg) {
        super(msg);
        this.message = msg;
    }
}
