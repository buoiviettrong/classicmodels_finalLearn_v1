package com.nixagh.classicmodels.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String productLineIsNotExisted) {
        super(productLineIsNotExisted);
    }
}
