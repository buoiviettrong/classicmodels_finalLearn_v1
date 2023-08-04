package com.nixagh.classicmodels.exception.exceptions;

import com.nixagh.classicmodels.exception.BaseException;
import org.springframework.security.core.AuthenticationException;

public class ExistingLoginException extends AuthenticationException implements BaseException {
    public ExistingLoginException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ExistingLoginException(String msg) {
        super(msg);
    }
}
