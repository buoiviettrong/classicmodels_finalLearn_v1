package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels.config.sercurity.JwtService;
import com.nixagh.classicmodels.dto.auth.AuthenticateRequest;
import com.nixagh.classicmodels.dto.auth.AuthenticationResponse;
import com.nixagh.classicmodels.dto.auth.RegisterRequest;
import com.nixagh.classicmodels.service.auth_service.AuthenticationService;
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
    private final JwtService jwtService;

    @PostMapping("/register")
    public AuthenticationResponse register(
            @RequestBody RegisterRequest request
    ) {
        return authenticationService.register(request);
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(
            @RequestBody AuthenticateRequest request
    ) throws IOException {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        String accessToken = authenticationResponse.getAccessToken();
        // get role from token
        String role = jwtService.getRoleFromToken(accessToken);
        authenticationResponse.setRedirect("/" + role.toLowerCase() + "/dashboard");
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
