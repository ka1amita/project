package com.gfa.exceptions.user;

import com.gfa.exceptions.AppRuntimeException;

public class InvalidPatchDataException extends AppRuntimeException {
    public InvalidPatchDataException(String message) {
        super(message);
    }
}
