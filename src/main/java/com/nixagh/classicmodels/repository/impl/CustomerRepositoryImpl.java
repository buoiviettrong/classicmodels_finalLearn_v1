package com.nixagh.classicmodels.repository.impl;

import com.nixagh.classicmodels.entity.Customer;
import com.nixagh.classicmodels.entity.Employee;
import com.nixagh.classicmodels.repository.CustomerRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class CustomerRepositoryImpl extends BaseRepositoryImpl<Customer, Long> implements CustomerRepository {
    public CustomerRepositoryImpl(EntityManager entityManager) {
        super(Customer.class, entityManager);
    }

    @Override
    public List<Customer> getCustomers() {
        return jpaQueryFactory
                .select(customer)
                .from(customer)
                .offset(0)
                .limit(10)
                .stream().toList();
    }

    @Override
    public List<Customer> getCustomersBySalesRepEmployeeNumber(Employee salesRepEmployeeNumber) {
        return jpaQueryFactory
                .selectFrom(customer)
                .where(customer.salesRepEmployeeNumber.eq(salesRepEmployeeNumber))
                .stream().toList();
    }

    @Override
    public Customer findByCustomerNumber(Long customerNumber) {
        return jpaQueryFactory
                .selectFrom(customer)
                .where(customer.customerNumber.eq(customerNumber))
                .fetchFirst();
    }
}
