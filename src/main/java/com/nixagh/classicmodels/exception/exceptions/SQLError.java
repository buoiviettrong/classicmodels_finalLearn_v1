package com.nixagh.classicmodels.exception.exceptions;


import com.nixagh.classicmodels.exception.BaseException;
import org.springframework.http.HttpStatus;

import java.sql.SQLIntegrityConstraintViolationException;

public class SQLError extends SQLIntegrityConstraintViolationException implements BaseException {
    public SQLError(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
