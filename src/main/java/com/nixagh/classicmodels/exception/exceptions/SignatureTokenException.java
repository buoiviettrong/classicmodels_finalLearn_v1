package com.nixagh.classicmodels.exception.exceptions;

import io.jsonwebtoken.security.SignatureException;

public class SignatureTokenException extends SignatureException {
    public SignatureTokenException(String message) {
        super(message);
    }
}
