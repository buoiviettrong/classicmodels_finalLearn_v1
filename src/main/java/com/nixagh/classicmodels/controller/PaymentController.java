package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels.entity.Payment;
import com.nixagh.classicmodels.service.PaymentService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
