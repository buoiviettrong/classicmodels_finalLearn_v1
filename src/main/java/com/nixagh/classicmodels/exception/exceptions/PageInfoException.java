package com.nixagh.classicmodels.exception.exceptions;

import com.nixagh.classicmodels.exception.BaseException;
import org.springframework.http.HttpStatus;

public class PageInfoException extends IllegalArgumentException implements BaseException {
    public PageInfoException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
