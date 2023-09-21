package com.gfa.exceptions;

public class InvalidActivationCodeException extends AppRuntimeException{
    public InvalidActivationCodeException(String message){
        super(message);
    }
}
