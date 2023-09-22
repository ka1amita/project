package com.gfa.exceptions;

public class AppRuntimeException extends RuntimeException {

    public AppRuntimeException() {
    }

    public AppRuntimeException(String message) {
        super(message);
    }

    public AppRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
