package com.nixagh.classicmodels.service.payment_service;

import com.nixagh.classicmodels.controller.PaymentController;
import com.nixagh.classicmodels.entity.Payment;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface IPaymentService {
    List<Payment> getAll();

    List<Payment> getByDateRange(Date start, Date end);

    List<Payment> getByCustomerNumber(Long customerNumber);

    ResponseEntity<?> createPayment(HttpServletRequest request, PaymentController.CreatePaymentRequest payment);

    void vnPayReturn(
            HttpServletResponse response,
            Long vnpAmount,
            String vnpBankCode,
            String vnpBankTranNo,
            String vnpCardType,
            String vnpOrderInfo,
            String vnpPayDate,
            String vnpResponseCode,
            String vnpTmnCode,
            String vnpTransactionNo,
            String vnpTxnRef,
            String vnpSecureHash,
            String vnpTransactionStatus
    ) throws ParseException, IOException;
}
