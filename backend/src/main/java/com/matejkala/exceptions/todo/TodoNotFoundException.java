package com.matejkala.exceptions.todo;

import com.matejkala.exceptions.AppRuntimeException;

public class TodoNotFoundException extends AppRuntimeException {
    public TodoNotFoundException(String message) {
        super(message);
    }
}
