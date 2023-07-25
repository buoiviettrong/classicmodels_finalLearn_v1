package com.nixagh.classicmodels.exception.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String productLineIsNotExisted) {
        super(productLineIsNotExisted);
    }
}
