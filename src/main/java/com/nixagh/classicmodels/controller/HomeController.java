package com.nixagh.classicmodels.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Hidden
public class HomeController {

  @GetMapping("/home")
  public String getHome() {
    return "Hello World!";
  }

  @GetMapping("/home/pin")
  public Principal getPrint(Principal principal) {
    return principal;
  }
}
