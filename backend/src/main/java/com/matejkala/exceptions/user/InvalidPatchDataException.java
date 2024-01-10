package com.matejkala.exceptions.user;

import com.matejkala.exceptions.AppRuntimeException;

public class InvalidPatchDataException extends AppRuntimeException {
    public InvalidPatchDataException(String message) {
        super(message);
    }
}
