package com.matejkala.exceptions.todo;

import com.matejkala.exceptions.AppRuntimeException;

public class TodoAlreadyExistsWithThisNameForThisUserException extends AppRuntimeException {
    public TodoAlreadyExistsWithThisNameForThisUserException(String message) {
        super(message);
    }
}
