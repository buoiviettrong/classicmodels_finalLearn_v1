package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels._common.auth.AuthenticateRequest;
import com.nixagh.classicmodels._common.auth.AuthenticationResponse;
import com.nixagh.classicmodels._common.auth.AuthenticationService;
import com.nixagh.classicmodels._common.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public AuthenticationResponse register(
            @RequestBody RegisterRequest request
    ) {
        return authenticationService.register(request);
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(
            @RequestBody AuthenticateRequest request
    ) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/refresh")
    public AuthenticationResponse refresh(
            @RequestBody refreshRequest refreshToken
    ) {
        return authenticationService.refresh(refreshToken.refreshToken);
    }

    public record refreshRequest(String refreshToken) {
    }
}
