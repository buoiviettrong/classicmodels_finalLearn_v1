package com.nixagh.classicmodels.repository.employer;

import com.nixagh.classicmodels.entity.Customer;
import com.nixagh.classicmodels.entity.Employee;
import com.nixagh.classicmodels.repository.BaseRepositoryImpl;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class EmployeeRepositoryImpl extends BaseRepositoryImpl<Employee, Long> implements EmployeeRepository {
    public EmployeeRepositoryImpl(EntityManager entityManager) {
        super(Employee.class, entityManager);
    }

    @Override
    public Optional<Employee> findByEmployeeNumber(Long eNum) {
        return jpaQueryFactory
                .selectFrom(employee)
                .where(employee.employeeNumber.eq(eNum))
                .stream().findFirst();
    }

    @Override
    public List<Employee> getEmployees() {
        return jpaQueryFactory
                .selectFrom(employee)
                .join(employee.officeCode, office).fetchJoin()
                .stream().toList();
    }

    @Override
    public long updateEmployee(Long employeeNumber, Employee e) {
        return jpaQueryFactory
                .update(employee)
                .set(employee.firstName, e.getFirstName())
                .set(employee.lastName, e.getLastName())
                .set(employee.extension, e.getExtension())
                .set(employee.email, e.getEmail())
                .set(employee.jobTitle, e.getJobTitle())
                .set(employee.officeCode, e.getOfficeCode())
                .set(employee.reportsTo, e.getReportsTo())
                .set(employee.employeeNumber, e.getEmployeeNumber())
                .where(employee.employeeNumber.eq(e.getEmployeeNumber()))
                .execute();
    }

    @Override
    public Optional<Customer> findOne(Long customerNumber) {
        return jpaQueryFactory
                .selectFrom(customer)
                .where(customer.customerNumber.eq(customerNumber))
                .stream().findFirst();
    }

}
