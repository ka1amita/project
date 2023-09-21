package com.gfa.exceptions.activation;

import com.gfa.exceptions.AppRuntimeException;

public class AccountAlreadyActiveException extends AppRuntimeException {
    public AccountAlreadyActiveException() {
    }

    public AccountAlreadyActiveException(String message) {
        super(message);
    }

    public AccountAlreadyActiveException(String message, Throwable cause) {
        super(message, cause);
    }
}
