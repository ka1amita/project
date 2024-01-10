package com.matejkala.exceptions.email;

import com.matejkala.exceptions.AppRuntimeException;

public class EmailAlreadyExistsException extends AppRuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
