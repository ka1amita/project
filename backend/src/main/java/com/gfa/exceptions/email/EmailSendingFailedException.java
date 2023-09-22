package com.gfa.exceptions.email;

import com.gfa.exceptions.AppRuntimeException;

public class EmailSendingFailedException extends AppRuntimeException {
    public EmailSendingFailedException(String message) {
        super(message);
    }
}
