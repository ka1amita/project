package com.gfa.exceptions.activation;

import com.gfa.exceptions.AppRuntimeException;

public class InvalidActivationCodeException extends AppRuntimeException {
    public InvalidActivationCodeException(String message){
        super(message);
    }
}
