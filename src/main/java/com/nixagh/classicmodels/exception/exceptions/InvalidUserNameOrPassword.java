package com.nixagh.classicmodels.exception.exceptions;

import com.nixagh.classicmodels.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class InvalidUserNameOrPassword extends AuthenticationException implements BaseException {
    public InvalidUserNameOrPassword(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidUserNameOrPassword(String msg) {
        super(msg);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
