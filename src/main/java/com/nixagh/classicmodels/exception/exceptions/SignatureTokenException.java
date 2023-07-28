package com.nixagh.classicmodels.exception.exceptions;

import com.nixagh.classicmodels.exception.BaseException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;

public class SignatureTokenException extends SignatureException implements BaseException {
    public SignatureTokenException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
