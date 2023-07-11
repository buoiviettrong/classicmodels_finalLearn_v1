package com.nixagh.classicmodels.repository.impl;

import com.nixagh.classicmodels.entity.Customer;
import com.nixagh.classicmodels.entity.Employee;
import com.nixagh.classicmodels.repository.CustomerRepository;
import com.querydsl.core.Tuple;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    @Override
    @Transactional
    public long deleteCustomer(long customerNumber) {
        return jpaQueryFactory.delete(customer)
                .where(customer.customerNumber.eq(customerNumber)).execute();
    }

    @Override
    public List<Tuple> getCustomerStatistical(Date from, Date to, long pageNumber, long pageSize) {
        long offset = (pageNumber - 1) * pageSize;
        long limit = pageSize;

        if (limit == 0) limit = 10;
        if (offset < 0) offset = 0;

        System.out.println("offset: " + offset);
        System.out.println("limit: " + limit);
        return jpaQueryFactory
                .select(
                        customer.customerName,
                        customer.customerNumber,
                        customer.ordersList.size(),
                        orderDetail.priceEach.multiply(orderDetail.quantityOrdered).sum()
                )
                .from(customer)
                .join(customer.ordersList, order)
                .join(order.orderDetail, orderDetail)
                .where(order.orderDate.between(from, to)
                        .and(order.status.eq("Shipped"))
                )
                .groupBy(customer.customerNumber)
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    public Long countCustomerStatistical(Date from, Date to) {
        return (long) jpaQueryFactory
                .select(
                        customer.customerNumber
                )
                .from(customer)
                .join(customer.ordersList, order)
                .join(order.orderDetail, orderDetail)
                .where(order.orderDate.between(from, to)
                        .and(order.status.eq("Shipped"))
                )
                .groupBy(customer.customerNumber)
                .fetch().size();
    }
}
