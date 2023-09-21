package com.gfa.exceptions.user;

import com.gfa.exceptions.AppRuntimeException;

public class InvalidPasswordFormatException extends AppRuntimeException {
    public InvalidPasswordFormatException() {
    }

    public InvalidPasswordFormatException(String message) {
        super(message);
    }

    public InvalidPasswordFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
