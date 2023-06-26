package com.nixagh.classicmodels.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GetPage {
  @GetMapping("/product")
  public String getProductPage() {
    return "product";
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

}
