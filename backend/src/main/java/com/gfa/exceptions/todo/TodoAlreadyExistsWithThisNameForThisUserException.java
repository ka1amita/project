package com.gfa.exceptions.todo;

import com.gfa.exceptions.AppRuntimeException;

public class TodoAlreadyExistsWithThisNameForThisUserException extends AppRuntimeException {
    public TodoAlreadyExistsWithThisNameForThisUserException(String message) {
        super(message);
    }
}
