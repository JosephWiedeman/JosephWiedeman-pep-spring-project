package com.example.exception;

public class MessageCreationException extends RuntimeException{
    private String message;

    public MessageCreationException() {}

    public MessageCreationException(String msg) {
        super(msg);
        this.message = msg;
    }
}
