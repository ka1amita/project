package com.gfa.exceptions.user;

import com.gfa.exceptions.AppRuntimeException;

public class UserNotFoundException extends AppRuntimeException {
    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
