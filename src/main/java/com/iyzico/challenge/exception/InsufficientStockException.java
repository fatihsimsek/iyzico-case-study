package com.iyzico.challenge.exception;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException() {
    }

    public InsufficientStockException(String message) {
        super(message);
    }
}
