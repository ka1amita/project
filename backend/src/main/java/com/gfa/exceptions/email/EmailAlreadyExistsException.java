package com.gfa.exceptions.email;

import com.gfa.exceptions.AppRuntimeException;

public class EmailAlreadyExistsException extends AppRuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
