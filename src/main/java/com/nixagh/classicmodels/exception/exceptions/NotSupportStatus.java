package com.nixagh.classicmodels.exception.exceptions;

import com.nixagh.classicmodels.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotSupportStatus extends IllegalArgumentException implements BaseException {
    public NotSupportStatus(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
