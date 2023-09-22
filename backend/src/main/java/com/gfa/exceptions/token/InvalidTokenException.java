package com.gfa.exceptions.token;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class InvalidTokenException extends JWTVerificationException {
    public InvalidTokenException(String message) {
        super(message);
    }
    public InvalidTokenException() {
        super("Authentication token is invalid");
    }
}
