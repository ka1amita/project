package com.gfa.exceptions.activation;

import com.gfa.exceptions.AppRuntimeException;

public class ActivationCodeExpiredException extends AppRuntimeException {
    public ActivationCodeExpiredException() {
    }

    public ActivationCodeExpiredException(String message) {
        super(message);
    }

    public ActivationCodeExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
