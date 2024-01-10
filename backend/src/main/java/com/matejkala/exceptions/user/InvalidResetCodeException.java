package com.matejkala.exceptions.user;

import com.matejkala.exceptions.AppRuntimeException;

public class InvalidResetCodeException extends AppRuntimeException {
    public InvalidResetCodeException(String message) {
        super(message);
    }

}
