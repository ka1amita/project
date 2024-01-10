package com.matejkala.exceptions.user;

import com.matejkala.exceptions.AppRuntimeException;

public class InvalidIdException extends AppRuntimeException {
    public InvalidIdException(String message) {
        super(message);
    }
}
