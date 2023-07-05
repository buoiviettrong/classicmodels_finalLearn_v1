package com.nixagh.classicmodels.exception;


public class InvalidToken extends IllegalArgumentException {
    public InvalidToken(String token) {
        super(token);
    }
}
