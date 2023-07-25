package com.nixagh.classicmodels.exception.exceptions;


import java.sql.SQLIntegrityConstraintViolationException;

public class SQLError extends SQLIntegrityConstraintViolationException {
    public SQLError(String message) {
        super(message);
    }
}
