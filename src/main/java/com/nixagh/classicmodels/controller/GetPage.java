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
        return "admin-order";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/statistic")
    public String getStatisticPage() {
        return "admin-statistic";
    }

    @GetMapping("/admin-order")
    public String getStatisticEachMonthPage() {
        return "admin-order";
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

    @GetMapping("/manager/order-history")
    public String getManagerOrderHistoryPage() {
        return "manager-order-history";
    }

    @GetMapping("/user/profile")
    public String getUserProfilePage() {
        return "profile";
    }

    @GetMapping("/chat")
    public String getChatPage() {
        return "chat";
    }
}
