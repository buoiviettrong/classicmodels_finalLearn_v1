package com.nixagh.classicmodels.exception.exceptions;

public class NotSupportStatus extends IllegalArgumentException {
    public NotSupportStatus(String message) {
        super(message);
    }

    public NotSupportStatus() {
    }
}
