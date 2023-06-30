package com.nixagh.classicmodels.exception;

import org.springframework.security.access.AccessDeniedException;

public class AccessDenied extends AccessDeniedException {
    public AccessDenied(String msg) {
        super(msg);
    }
}
