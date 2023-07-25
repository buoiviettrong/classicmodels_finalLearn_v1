package com.nixagh.classicmodels.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HomeController {

    @GetMapping("/home")
    @PreAuthorize("hasAuthority('user:read')")
    public String getHome() {
        return "Hello World!";
    }

    @GetMapping("/home/pin")
    public Principal getPrint(Principal principal) {
        return principal;
    }
}
