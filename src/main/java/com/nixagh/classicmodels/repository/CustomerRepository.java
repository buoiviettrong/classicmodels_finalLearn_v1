package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.Customer;
import com.nixagh.classicmodels.entity.Employee;
import com.nixagh.classicmodels.entity.Order;

import java.util.List;

public interface CustomerRepository extends BaseRepository<Customer, Long> {
	public List<Customer> getCustomers();

	List<Customer> getCustomersBySalesRepEmployeeNumber(Employee salesRepEmployeeNumber);

	Customer findByCustomerNumber(Long customerNumber);
}
