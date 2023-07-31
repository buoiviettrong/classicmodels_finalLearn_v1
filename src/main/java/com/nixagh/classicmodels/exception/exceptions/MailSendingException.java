package com.nixagh.classicmodels.exception.exceptions;

import org.springframework.mail.MailSendException;

public class MailSendingException extends MailSendException {
    public MailSendingException(String message) {
        super(message);
    }
}
