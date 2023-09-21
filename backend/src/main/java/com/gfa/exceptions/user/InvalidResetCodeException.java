package com.gfa.exceptions.user;

import com.gfa.exceptions.AppRuntimeException;

public class InvalidResetCodeException extends AppRuntimeException {
    public InvalidResetCodeException(String message) {
        super(message);
    }

}
