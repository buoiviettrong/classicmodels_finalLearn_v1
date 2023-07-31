package com.nixagh.classicmodels.service.mail_service;

public interface IMailService {
    void sendPaymentReceiptMail();
    void sendPaymentReceiptMail(PaymentReceipt paymentReceipt);
}
