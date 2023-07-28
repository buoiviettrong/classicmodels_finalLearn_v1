package com.nixagh.classicmodels.exception.exceptions;

import com.nixagh.classicmodels.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotEnoughMoney extends RuntimeException implements BaseException {
    public NotEnoughMoney(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.PAYMENT_REQUIRED;
    }
}
