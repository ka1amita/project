package com.matejkala.exceptions.role;

import com.matejkala.exceptions.AppRuntimeException;

public class RoleNotFoundException extends AppRuntimeException {
     public RoleNotFoundException(String message) {
        super(message);
    }
}
