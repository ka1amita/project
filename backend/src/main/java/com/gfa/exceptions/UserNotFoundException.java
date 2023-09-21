package com.gfa.exceptions;

public class UserNotFoundException extends AppRuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
