package com.matejkala.exceptions.email;

import com.matejkala.exceptions.AppRuntimeException;

public class EmailFormatException extends AppRuntimeException {
    public EmailFormatException(String message) {
        super(message);
    }
}
