package com.matejkala.exceptions.user;

import com.matejkala.exceptions.AppRuntimeException;

public class InvalidPasswordFormatException extends AppRuntimeException {
    public InvalidPasswordFormatException(String message) {
        super(message);
    }

}
