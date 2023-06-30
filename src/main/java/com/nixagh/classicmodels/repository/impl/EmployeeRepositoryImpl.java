package com.nixagh.classicmodels.repository.impl;

import com.nixagh.classicmodels.entity.Employee;
import com.nixagh.classicmodels.repository.EmployeeRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class EmployeeRepositoryImpl extends BaseRepositoryImpl<Employee, Long> implements EmployeeRepository {
    public EmployeeRepositoryImpl(EntityManager entityManager) {
        super(Employee.class, entityManager);
    }

    @Override
    public Employee findByEmployeeNumber(Long eNum) {
        return jpaQueryFactory
                .selectFrom(employee)
                .where(employee.employeeNumber.eq(eNum))
                .fetchOne();
    }

    @Override
    public List<Employee> getEmployees() {
        return jpaQueryFactory
                .selectFrom(employee)
                .stream().toList();
    }


}
