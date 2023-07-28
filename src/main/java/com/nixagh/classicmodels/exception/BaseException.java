package com.nixagh.classicmodels.exception;

import org.springframework.http.HttpStatus;

public interface BaseException {
    default HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
