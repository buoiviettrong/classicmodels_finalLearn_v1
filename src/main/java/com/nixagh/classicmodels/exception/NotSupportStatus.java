package com.nixagh.classicmodels.exception;

public class NotSupportStatus extends IllegalArgumentException {
    public NotSupportStatus(String message) {
        super(message);
    }

    public NotSupportStatus() {
    }
}
