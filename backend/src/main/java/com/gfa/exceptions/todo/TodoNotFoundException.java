package com.gfa.exceptions.todo;

import com.gfa.exceptions.AppRuntimeException;

public class TodoNotFoundException extends AppRuntimeException {
    public TodoNotFoundException(String message) {
        super(message);
    }
}
