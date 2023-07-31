package com.nixagh.classicmodels.service.mail_service;

public interface IMailService {
    String sendPaymentReceiptMail(String to);

    void setPaymentReceipt(PaymentReceipt paymentReceipt);
}
