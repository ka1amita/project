package com.gfa.exceptions.user;

import com.gfa.exceptions.AppRuntimeException;

public class UserAlreadyExistsException extends AppRuntimeException {
    public UserAlreadyExistsException(String message){
        super(message);
    }



}
