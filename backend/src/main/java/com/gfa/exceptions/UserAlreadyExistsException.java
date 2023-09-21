package com.gfa.exceptions;

public class UserAlreadyExistsException extends AppRuntimeException{
    public UserAlreadyExistsException(String message){
        super(message);
    }
}
