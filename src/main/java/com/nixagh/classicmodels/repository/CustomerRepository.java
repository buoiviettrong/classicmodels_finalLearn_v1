package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.Customer;
import com.nixagh.classicmodels.entity.Employee;
import com.querydsl.core.Tuple;

import java.util.Date;
import java.util.List;

public interface CustomerRepository extends BaseRepository<Customer, Long> {
    List<Customer> getCustomers();

    List<Customer> getCustomersBySalesRepEmployeeNumber(Employee salesRepEmployeeNumber);

    Customer findByCustomerNumber(Long customerNumber);

    long deleteCustomer(long customerNumber);

    List<Tuple> getCustomerStatistical(Date from, Date to, long pageNumber, long pageSize);

    Long countCustomerStatistical(Date from, Date to);

}
