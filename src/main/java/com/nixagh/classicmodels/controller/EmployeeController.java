package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels.entity.Employee;
import com.nixagh.classicmodels.exception.NotFoundEntity;
import com.nixagh.classicmodels.repository.EmployeeRepository;
import com.nixagh.classicmodels.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employee")
@CrossOrigin(origins = "*")
@EnableCaching
@PreAuthorize("hasRole('ADMIN')")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    @Cacheable(value = "employees", cacheManager = "cacheManager")
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    @GetMapping("/{employeeNumber}")
    @Cacheable(value = "employee", key = "#employeeNumber.toString()", cacheManager = "cacheManager")
    public Employee getEmployeeById(@PathVariable Long employeeNumber) {
        return employeeService.findById(employeeNumber);
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }

    @PutMapping("/{employeeNumber}")
    @CachePut(value = "employee", key = "#employeeNumber.toString()", cacheManager = "cacheManager")
    public Employee updateEmployee(@PathVariable Long employeeNumber, @RequestBody Employee employee) {
        return employeeService.updateEmployee(employeeNumber, employee);
    }

    @DeleteMapping("/{employeeNumber}")
    @CacheEvict(value = "employee", key = "#employeeNumber.toString()", cacheManager = "cacheManager")
    public void deleteEmployee(@PathVariable Long employeeNumber) {
        employeeService.deleteEmployee(employeeNumber);
    }
}
