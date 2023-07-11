package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels._common.auth.AuthenticateRequest;
import com.nixagh.classicmodels._common.auth.AuthenticationResponse;
import com.nixagh.classicmodels._common.auth.AuthenticationService;
import com.nixagh.classicmodels._common.auth.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
            @RequestBody AuthenticateRequest request,
            HttpServletResponse response
    ) throws IOException {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        String role = authenticationResponse.getUserDetails().getRole().getRoleName().toLowerCase();
        authenticationResponse.setRedirect("/" + role + "/dashboard");
        return authenticationResponse;
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
