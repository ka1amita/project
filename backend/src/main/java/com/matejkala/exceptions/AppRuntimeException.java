package com.matejkala.exceptions;

public class AppRuntimeException extends RuntimeException {
    public AppRuntimeException(String message) {
        super(message);
    }
}
