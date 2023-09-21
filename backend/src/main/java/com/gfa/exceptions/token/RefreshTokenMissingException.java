package com.gfa.exceptions.token;

import com.gfa.exceptions.AppRuntimeException;

public class RefreshTokenMissingException extends AppRuntimeException {
    public RefreshTokenMissingException(String message) {
        super(message);
    }
}
