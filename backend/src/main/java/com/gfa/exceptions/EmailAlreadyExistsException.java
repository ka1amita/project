package com.gfa.exceptions;

public class EmailAlreadyExistsException extends AppRuntimeException{
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
