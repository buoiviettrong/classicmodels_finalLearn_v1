package com.nixagh.classicmodels.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GetPage {
    @GetMapping("/product")
    public String getProductPage() {
        return "product";
    }

    @GetMapping("/product-line")
    public String getProductLinePage() {
        return "product-line";
    }

    @GetMapping("/customer")
    public String getCustomerPage() {
        return "customer";
    }

    @GetMapping("/order")
    public String getOrderPage() {
        return "order";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/statistic")
    public String getStatisticPage() {
        return "statistic";
    }

    @GetMapping("/statistic-each-month")
    public String getStatisticEachMonthPage() {
        return "statistic-each-month";
    }

    @GetMapping("/admin/dashboard")
    public String getEmployeePage() {
        return "admin";
    }

    @GetMapping("/manager/dashboard")
    public String getManagerPage() {
        return "manager";
    }

    @GetMapping("/manager/product")
    public String getManagerProductPage() {
        return "manager-product";
    }

}
