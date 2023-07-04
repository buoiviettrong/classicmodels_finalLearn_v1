package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends BaseRepository<Employee, Long> {
    Optional<Employee> findByEmployeeNumber(Long eNum);

    List<Employee> getEmployees();

    long updateEmployee(Long employeeNumber, Employee employee);
}
