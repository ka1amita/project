package com.gfa.exceptions.user;

import com.gfa.exceptions.AppRuntimeException;

public class InvalidPasswordFormatException extends AppRuntimeException {
    public InvalidPasswordFormatException(String message) {
        super(message);
    }

}
