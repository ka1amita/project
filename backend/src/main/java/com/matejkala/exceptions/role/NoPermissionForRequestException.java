package com.matejkala.exceptions.role;

import com.matejkala.exceptions.AppRuntimeException;

public class NoPermissionForRequestException extends AppRuntimeException {
    public NoPermissionForRequestException(String message) {
        super(message);
    }
}
