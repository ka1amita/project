package com.gfa.exceptions;

public class InvalidActivationCodeException extends RuntimeException{
    public InvalidActivationCodeException(String message){
        super(message);
    }
}
