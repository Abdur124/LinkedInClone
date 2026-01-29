package com.springboot.clone.linkedin.user_service.exceptions;

public class BadCredentialsException extends RuntimeException {

    public BadCredentialsException(String message) {
        super(message);
    }

    public BadCredentialsException() {

    }
}
