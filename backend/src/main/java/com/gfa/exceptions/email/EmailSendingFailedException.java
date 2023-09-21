package com.gfa.exceptions.email;

import com.gfa.exceptions.AppRuntimeException;

public class EmailSendingFailedException extends AppRuntimeException {
    public EmailSendingFailedException() {
    }

    public EmailSendingFailedException(String message) {
        super(message);
    }

    public EmailSendingFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
