package com.matejkala.exceptions.user;

import com.matejkala.exceptions.AppRuntimeException;

public class MissingJSONBodyException extends AppRuntimeException {
    public MissingJSONBodyException(String message) {
        super(message);
    }
}
