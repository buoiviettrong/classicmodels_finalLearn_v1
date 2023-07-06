package com.nixagh.classicmodels.exception;

public class NotEnoughProduct extends IllegalArgumentException {
    public NotEnoughProduct(String formatted) {
        super(formatted);
    }
}
