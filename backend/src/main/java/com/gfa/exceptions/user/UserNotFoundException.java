package com.gfa.exceptions.user;

import com.gfa.exceptions.AppRuntimeException;

public class UserNotFoundException extends AppRuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
