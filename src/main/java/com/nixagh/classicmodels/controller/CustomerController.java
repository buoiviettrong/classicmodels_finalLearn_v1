package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels.entity.Customer;
import com.nixagh.classicmodels.service.CustomerService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/filters")
    public List<Customer> getCustomersBySalesRepEmployeeNumber(@PathParam("eNum") Long eNum) throws IllegalAccessException {
        return customerService.getCustomersBySalesRepEmployeeNumber(eNum);
    }
}
