package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels.entity.Payment;
import com.nixagh.classicmodels.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public List<Payment> getAll() {
        return paymentService.getAll();
    }

    @GetMapping("/{customerNumber}")
    public List<Payment> getByCustomerNumber(@PathVariable Long customerNumber) {
        return paymentService.getByCustomerNumber(customerNumber);
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public List<Payment> getByDateRange(
            @PathParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
            @PathParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end
    ) {
        return paymentService.getByDateRange(start, end);
    }

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(
            HttpServletRequest request,
            @RequestBody CreatePaymentRequest payment
    ) {
        return paymentService.createPayment(request, payment);
    }

    public record CreatePaymentRequest(
            Long customerNumber,
            String orderNumber,
            Double amount
    ) {
    }

    @GetMapping("/vnpay_return")
    public void vnPayReturn(
            @PathParam("vnp_Amount") Long vnp_Amount,
            @PathParam("vnp_BankCode") String vnp_BankCode,
            @PathParam("vnp_BankTranNo") String vnp_BankTranNo,
            @PathParam("vnp_CardType") String vnp_CardType,
            @PathParam("vnp_OrderInfo") String vnp_OrderInfo,
            @PathParam("vnp_PayDate") String vnp_PayDate,
            @PathParam("vnp_ResponseCode") String vnp_ResponseCode,
            @PathParam("vnp_TmnCode") String vnp_TmnCode,
            @PathParam("vnp_TransactionNo") String vnp_TransactionNo,
            @PathParam("vnp_TxnRef") String vnp_TxnRef,
            @PathParam("vnp_SecureHash") String vnp_SecureHash,
            @PathParam("vnp_TransactionStatus") String vnp_TransactionStatus,
            HttpServletResponse response
    ) throws ParseException, IOException {

        paymentService.vnPayReturn(
                response,
                vnp_Amount,
                vnp_BankCode,
                vnp_BankTranNo,
                vnp_CardType,
                vnp_OrderInfo,
                vnp_PayDate,
                vnp_ResponseCode,
                vnp_TmnCode,
                vnp_TransactionNo,
                vnp_TxnRef,
                vnp_SecureHash,
                vnp_TransactionStatus
        );
    }
}
