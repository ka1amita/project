package com.matejkala.exceptions.token;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class MissingBearerTokenException extends JWTVerificationException {
    public MissingBearerTokenException(String message) {
        super(message);
    }
}
