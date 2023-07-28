package com.nixagh.classicmodels.exception.exceptions;

import com.nixagh.classicmodels.exception.BaseException;

public class NotFoundRole extends NotFoundEntity implements BaseException {
    public NotFoundRole(String message) {
        super(message);
    }
}
