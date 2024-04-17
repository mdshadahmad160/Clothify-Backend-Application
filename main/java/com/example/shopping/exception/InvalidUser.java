package com.example.shopping.exception;

public class InvalidUser extends RuntimeException{
    private final String errorMessage;

    public InvalidUser(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

}
