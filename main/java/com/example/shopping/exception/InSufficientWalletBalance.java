package com.example.shopping.exception;

import lombok.Data;

@Data
public class InSufficientWalletBalance extends RuntimeException{

    private final String errorMessage;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public InSufficientWalletBalance(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
