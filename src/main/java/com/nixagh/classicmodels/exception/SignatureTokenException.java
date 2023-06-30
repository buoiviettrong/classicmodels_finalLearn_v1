package com.nixagh.classicmodels.exception;

import io.jsonwebtoken.security.SignatureException;

public class SignatureTokenException extends SignatureException {
    public SignatureTokenException(String message) {
        super(message);
    }
}
