package com.nixagh.classicmodels.exception;

// quantity of product is not enough
public class NotEnoughProduct extends IllegalArgumentException {
    public NotEnoughProduct(String formatted) {
        super(formatted);
    }
}
