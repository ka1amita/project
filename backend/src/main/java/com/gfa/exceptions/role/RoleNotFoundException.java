package com.gfa.exceptions.role;

import com.gfa.exceptions.AppRuntimeException;

public class RoleNotFoundException extends AppRuntimeException {
     public RoleNotFoundException(String message) {
        super(message);
    }
}
