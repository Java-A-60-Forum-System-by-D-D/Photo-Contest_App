package com.example.demo.exceptions;

public class EntityDuplicateException extends RuntimeException {
    public EntityDuplicateException(String message) {
        super(message);
    }
}
