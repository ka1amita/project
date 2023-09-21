package com.gfa.exceptions.activation;

import com.gfa.exceptions.AppRuntimeException;

public class AccountAlreadyActiveException extends AppRuntimeException {
    public AccountAlreadyActiveException(String message) {
        super(message);
    }

}
