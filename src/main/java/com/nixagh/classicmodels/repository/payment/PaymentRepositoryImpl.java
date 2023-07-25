package com.nixagh.classicmodels.repository.payment;

import com.nixagh.classicmodels.entity.Payment;
import com.nixagh.classicmodels.repository.BaseRepositoryImpl;
import jakarta.persistence.EntityManager;

import java.util.Date;
import java.util.List;

public class PaymentRepositoryImpl extends BaseRepositoryImpl<Payment, Long> implements PaymentRepository {
    public PaymentRepositoryImpl(EntityManager entityManager) {
        super(Payment.class, entityManager);
    }

    @Override
    public List<Payment> getAll() {
        return jpaQueryFactory
                .selectFrom(payment)
                .leftJoin(payment.customer, customer).fetchJoin()
                .leftJoin(customer.salesRepEmployeeNumber, employee).fetchJoin()
                .leftJoin(employee.officeCode, office).fetchJoin()
                .stream().toList();
    }

    @Override
    public List<Payment> getByCustomerNumber(Long customerNumber) {
        return jpaQueryFactory
                .selectFrom(payment)
                .leftJoin(payment.customer, customer).fetchJoin()
                .leftJoin(customer.salesRepEmployeeNumber, employee).fetchJoin()
                .leftJoin(employee.officeCode, office).fetchJoin()
                .where(customer.customerNumber.eq(customerNumber))
                .stream().toList();
    }

    @Override
    public List<Payment> getByPaymentDateBetween(Date start, Date end) {
        return jpaQueryFactory
                .selectFrom(payment)
                .leftJoin(payment.customer, customer).fetchJoin()
                .leftJoin(customer.salesRepEmployeeNumber, employee).fetchJoin()
                .leftJoin(employee.officeCode, office).fetchJoin()
                .where(payment.paymentDate.between(start, end))
                .stream().toList();
    }
}
