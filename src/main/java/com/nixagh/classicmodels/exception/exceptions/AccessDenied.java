package com.nixagh.classicmodels.exception.exceptions;

import com.nixagh.classicmodels.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

public class AccessDenied extends AccessDeniedException implements BaseException {
    public AccessDenied(String msg) {
        super(msg);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_ACCEPTABLE;
    }
}
