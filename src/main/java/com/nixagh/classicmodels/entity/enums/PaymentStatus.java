package com.nixagh.classicmodels.entity.enums;

public enum PaymentStatus {
    PAID("Paid"),
    UNPAID("Unpaid");

    private final String paymentStatus;

    PaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }
}
