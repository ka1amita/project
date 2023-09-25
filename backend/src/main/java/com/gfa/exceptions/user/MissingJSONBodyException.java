package com.gfa.exceptions.user;

import com.gfa.exceptions.AppRuntimeException;

public class MissingJSONBodyException extends AppRuntimeException {
    public MissingJSONBodyException(String message) {
        super(message);
    }
}
