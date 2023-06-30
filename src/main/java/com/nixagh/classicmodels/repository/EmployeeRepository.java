package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends BaseRepository<Employee, Long> {
    Employee findByEmployeeNumber(Long eNum);

    List<Employee> getEmployees();
}
