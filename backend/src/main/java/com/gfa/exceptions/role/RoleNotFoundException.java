package com.gfa.exceptions.role;

import com.gfa.exceptions.AppRuntimeException;

public class RoleNotFoundException extends AppRuntimeException {
    public RoleNotFoundException() {
    }

    public RoleNotFoundException(String message) {
        super(message);
    }

    public RoleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
