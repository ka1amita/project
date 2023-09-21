package com.gfa.exceptions.token;

import com.gfa.exceptions.AppRuntimeException;

public class RefreshTokenMissingException extends AppRuntimeException {
    public RefreshTokenMissingException() {
    }

    public RefreshTokenMissingException(String message) {
        super(message);
    }

    public RefreshTokenMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
