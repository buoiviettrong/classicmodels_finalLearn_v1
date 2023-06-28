package com.nixagh.classicmodels.exception;

import com.nixagh.classicmodels.dto.PageRequestInfo;

public class PageInfoException extends IllegalArgumentException{
    public PageInfoException(String message) {
        super(message);
    }
}
