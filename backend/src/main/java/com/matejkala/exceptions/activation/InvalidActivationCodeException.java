package com.matejkala.exceptions.activation;

import com.matejkala.exceptions.AppRuntimeException;

public class InvalidActivationCodeException extends AppRuntimeException {
    public InvalidActivationCodeException(String message){
        super(message);
    }
}
