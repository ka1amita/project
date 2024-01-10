package com.matejkala.exceptions.user;

import com.matejkala.exceptions.AppRuntimeException;

public class UserAlreadyExistsException extends AppRuntimeException {
    public UserAlreadyExistsException(String message){
        super(message);
    }
    }
