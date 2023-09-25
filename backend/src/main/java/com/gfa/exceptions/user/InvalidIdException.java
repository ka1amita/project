package com.gfa.exceptions.user;

import com.gfa.exceptions.AppRuntimeException;

public class InvalidIdException extends AppRuntimeException {
    public InvalidIdException(String message) {
        super(message);
    }
}
