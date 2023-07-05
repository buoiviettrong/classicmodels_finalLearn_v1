package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.Payment;

import java.util.Date;
import java.util.List;

public interface PaymentRepository extends BaseRepository<Payment, Long> {
    List<Payment> getAll();

    List<Payment> getByCustomerNumber(Long customerNumber);

    List<Payment> getByPaymentDateBetween(Date start, Date end);
}
