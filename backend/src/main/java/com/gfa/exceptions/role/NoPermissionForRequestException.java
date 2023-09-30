package com.gfa.exceptions.role;

import com.gfa.exceptions.AppRuntimeException;

public class NoPermissionForRequestException extends AppRuntimeException {
    public NoPermissionForRequestException(String message) {
        super(message);
    }
}
