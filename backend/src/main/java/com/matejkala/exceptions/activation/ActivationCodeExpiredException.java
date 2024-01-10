package com.matejkala.exceptions.activation;

import com.matejkala.exceptions.AppRuntimeException;

public class ActivationCodeExpiredException extends AppRuntimeException {
    public ActivationCodeExpiredException(String message) {
        super(message);
    }

}
