package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.Customer;
import com.nixagh.classicmodels.entity.Employee;

import java.util.List;

public interface CustomerRepository extends BaseRepository<Customer, Long> {
  List<Customer> getCustomers();

  List<Customer> getCustomersBySalesRepEmployeeNumber(Employee salesRepEmployeeNumber);

  Customer findByCustomerNumber(Long customerNumber);
}
