package com.matejkala.exceptions.user;

import com.matejkala.exceptions.AppRuntimeException;

public class UserNotFoundException extends AppRuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

}
