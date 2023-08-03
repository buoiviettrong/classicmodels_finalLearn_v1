package com.nixagh.classicmodels._commons.constants.errorConstants;

import lombok.AllArgsConstructor;

public class BaseError {
    public String statusCode = "500";
    public String message = "Something went wrong";

    public BaseError(String code, String message) {
        this.statusCode = code;
        this.message = message;
    }

    public BaseError() {
    }
}
