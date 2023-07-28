package com.nixagh.classicmodels.exception.exceptions;

import com.nixagh.classicmodels.exception.BaseException;
import org.springframework.http.HttpStatus;

// quantity of product is not enough
public class NotEnoughProduct extends IllegalArgumentException implements BaseException {
    public NotEnoughProduct(String formatted) {
        super(formatted);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
