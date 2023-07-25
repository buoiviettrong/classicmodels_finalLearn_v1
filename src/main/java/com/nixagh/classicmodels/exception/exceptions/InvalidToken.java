package com.nixagh.classicmodels.exception.exceptions;


public class InvalidToken extends IllegalArgumentException {
    public InvalidToken(String token) {
        super(token);
    }
}
