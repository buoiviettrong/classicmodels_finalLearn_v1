package com.nixagh.classicmodels.service;

import com.nixagh.classicmodels.entity.Employee;
import com.nixagh.classicmodels.exception.NotFoundEntity;
import com.nixagh.classicmodels.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public List<Employee> getEmployees() {
        return employeeRepository.getEmployees();
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee findById(Long employeeNumber) {
        return employeeRepository.findByEmployeeNumber(employeeNumber).orElseThrow(() -> new NotFoundEntity("Employee not found"));
    }


    public Employee updateEmployee(Long employeeNumber, Employee employee) {
        Employee employeeToUpdate = findById(employeeNumber);

        // update employee to employeeToUpdate with not null values from employee
        employeeToUpdate.setFirstName(Optional.ofNullable(employee.getFirstName()).orElse(employeeToUpdate.getFirstName()));
        employeeToUpdate.setLastName(Optional.ofNullable(employee.getLastName()).orElse(employeeToUpdate.getLastName()));
        employeeToUpdate.setExtension(Optional.ofNullable(employee.getExtension()).orElse(employeeToUpdate.getExtension()));
        employeeToUpdate.setEmail(Optional.ofNullable(employee.getEmail()).orElse(employeeToUpdate.getEmail()));
        employeeToUpdate.setJobTitle(Optional.ofNullable(employee.getJobTitle()).orElse(employeeToUpdate.getJobTitle()));
        employeeToUpdate.setOfficeCode(Optional.ofNullable(employee.getOfficeCode()).orElse(employeeToUpdate.getOfficeCode()));
        employeeToUpdate.setReportsTo(Optional.ofNullable(employee.getReportsTo()).orElse(employeeToUpdate.getReportsTo()));
        employeeToUpdate.setEmployeeNumber(Optional.ofNullable(employee.getEmployeeNumber()).orElse(employeeToUpdate.getEmployeeNumber()));

        save(employeeToUpdate);
        return employeeToUpdate;
    }

    public void deleteEmployee(Long employeeNumber) {
        employeeRepository.delete(findById(employeeNumber));
    }
}
