package com.gfa.exceptions.user;

import com.gfa.exceptions.AppRuntimeException;

public class InvalidResetCodeException extends AppRuntimeException {
    public InvalidResetCodeException() {
    }

    public InvalidResetCodeException(String message) {
        super(message);
    }

    public InvalidResetCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
