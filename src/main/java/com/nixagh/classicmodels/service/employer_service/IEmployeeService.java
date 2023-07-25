package com.nixagh.classicmodels.service.employer_service;

import com.nixagh.classicmodels.entity.Employee;

import java.util.List;

public interface IEmployeeService {
    List<Employee> getEmployees();

    Employee save(Employee employee);

    Employee getEmployeeById(Long employeeNumber);

    Employee updateEmployee(Long employeeNumber, Employee employee);

    void deleteEmployee(Long employeeNumber);
}
