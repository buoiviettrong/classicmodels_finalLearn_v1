package com.nixagh.classicmodels.repository.employer;

import com.nixagh.classicmodels.entity.Customer;
import com.nixagh.classicmodels.entity.Employee;
import com.nixagh.classicmodels.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends BaseRepository<Employee, Long> {
    Optional<Employee> findByEmployeeNumber(Long eNum);

    List<Employee> getEmployees();

    long updateEmployee(Long employeeNumber, Employee employee);

    Optional<Customer> findOne(Long customerNumber);
}
