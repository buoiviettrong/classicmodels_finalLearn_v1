package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels.entity.Customer;
import com.nixagh.classicmodels.service.customer_service.CustomerService;
import com.nixagh.classicmodels.service.customer_service.ICustomerService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@PreAuthorize("hasRole('ADMIN')")
public class CustomerController {

    private final ICustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/filters")
    public List<Customer> getCustomersBySalesRepEmployeeNumber(@PathParam("eNum") Long eNum) throws IllegalAccessException {
        return customerService.getCustomersBySalesRepEmployeeNumber(eNum);
    }

    @DeleteMapping("/{customerNumber}")
    public Long deleteCustomer(@PathVariable("customerNumber") long customerNumber) {
        return customerService.deleteCustomer(customerNumber);
    }
}
