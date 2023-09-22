package com.gfa.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class MissingBearerTokenException extends JWTVerificationException {
    public MissingBearerTokenException(String message) {
        super(message);
    }

    public MissingBearerTokenException() {
        super("Authentication token is required");
    }
}
