package com.example.demo.domain.exception;

public class EmptyUpdateFieldsException extends RuntimeException {
    public EmptyUpdateFieldsException(String message) {
        super(message);
    }
}
