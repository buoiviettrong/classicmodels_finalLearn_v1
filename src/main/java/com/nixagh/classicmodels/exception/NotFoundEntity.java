package com.nixagh.classicmodels.exception;

public class NotFoundEntity extends RuntimeException {
    public NotFoundEntity(String message) {
        super(message);
    }
}
