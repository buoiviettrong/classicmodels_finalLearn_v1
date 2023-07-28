package com.nixagh.classicmodels.exception.exceptions;


import com.nixagh.classicmodels.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidToken extends IllegalArgumentException implements BaseException {
    public InvalidToken(String token) {
        super(token);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
