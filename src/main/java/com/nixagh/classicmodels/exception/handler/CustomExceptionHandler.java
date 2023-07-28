package com.nixagh.classicmodels.exception.handler;

import com.nixagh.classicmodels.exception.BaseException;
import com.nixagh.classicmodels.exception.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(Exception.class)
    public <T extends Exception & BaseException> ResponseEntity<ErrorResponse> handlerException(T e) {
        var errorResponse = new ErrorResponse(e.getStatus(), e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(errorResponse);
    }
}
