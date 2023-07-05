package com.nixagh.classicmodels.service;

import com.nixagh.classicmodels.entity.Payment;
import com.nixagh.classicmodels.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public List<Payment> getAll() {
        return paymentRepository.getAll();
    }

    public List<Payment> getByDateRange(Date start, Date end) {
        return paymentRepository.getByPaymentDateBetween(start, end);
    }

    public List<Payment> getByCustomerNumber(Long customerNumber) {
        return paymentRepository.getByCustomerNumber(customerNumber);
    }
}
