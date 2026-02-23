package com.springboot.clone.linkedin.posts_service.exceptions;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException() {

    }
}
