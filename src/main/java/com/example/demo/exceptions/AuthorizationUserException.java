package com.example.demo.exceptions;

public class AuthorizationUserException extends RuntimeException {
    public AuthorizationUserException(String message) {
        super(message);
    }
}
