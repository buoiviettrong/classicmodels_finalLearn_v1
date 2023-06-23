package com.nixagh.classicmodels.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
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
