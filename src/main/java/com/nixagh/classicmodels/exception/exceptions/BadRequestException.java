package com.nixagh.classicmodels.exception.exceptions;

import com.nixagh.classicmodels.exception.BaseException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException implements BaseException {
    public BadRequestException(String productLineIsNotExisted) {
        super(productLineIsNotExisted);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
