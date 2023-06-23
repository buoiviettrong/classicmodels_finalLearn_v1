package com.nixagh.classicmodels.service;

import com.nixagh.classicmodels.entity.Customer;
import com.nixagh.classicmodels.entity.Employee;
import com.nixagh.classicmodels.repository.CustomerRepository;
import com.nixagh.classicmodels.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
  private final CustomerRepository customerRepository;
  private final EmployeeRepository employeeRepository;

  public List<Customer> getCustomers() {
    return customerRepository.getCustomers();
  }


  public List<Customer> getCustomersBySalesRepEmployeeNumber(Long eNum) throws IllegalAccessException {
    Employee employee = employeeRepository.findByEmployeeNumber(eNum);
    return customerRepository.getCustomersBySalesRepEmployeeNumber(employee);
  }
}
