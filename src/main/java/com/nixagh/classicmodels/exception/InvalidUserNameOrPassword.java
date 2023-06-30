package com.nixagh.classicmodels.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidUserNameOrPassword extends AuthenticationException {
    public InvalidUserNameOrPassword(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidUserNameOrPassword(String msg) {
        super(msg);
    }
}
