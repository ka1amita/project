package com.gfa.exceptions.email;

import com.gfa.exceptions.AppRuntimeException;

public class EmailFormatException extends AppRuntimeException {
    public EmailFormatException(String message) {
        super(message);
    }
}
