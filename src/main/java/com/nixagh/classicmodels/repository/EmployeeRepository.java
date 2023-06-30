package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.Employee;

public interface EmployeeRepository extends BaseRepository<Employee, Long> {
    Employee findByEmployeeNumber(Long eNum);
}
