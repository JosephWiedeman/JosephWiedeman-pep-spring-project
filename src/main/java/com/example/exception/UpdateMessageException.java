package com.example.exception;

public class UpdateMessageException extends RuntimeException{
    private String message;
    
    public UpdateMessageException() {}
    
    public UpdateMessageException(String msg) {
        super(msg);
        this.message = msg;
    }
}
