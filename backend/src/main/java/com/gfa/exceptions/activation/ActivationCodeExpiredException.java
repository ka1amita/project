package com.gfa.exceptions.activation;

import com.gfa.exceptions.AppRuntimeException;

public class ActivationCodeExpiredException extends AppRuntimeException {
    public ActivationCodeExpiredException(String message) {
        super(message);
    }

}
