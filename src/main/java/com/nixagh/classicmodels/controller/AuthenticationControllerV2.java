package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels.config.sercurity.JwtService;
import com.nixagh.classicmodels.dto.auth.AuthenticateRequest;
import com.nixagh.classicmodels.dto.auth.AuthenticationResponse;
import com.nixagh.classicmodels.service.auth_service.IAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
public class AuthenticationControllerV2 {

    private final IAuthenticationService authenticationService;
    private final JwtService jwtService;
    @PostMapping("/authenticate-only-one-device")
    public AuthenticationResponse authenticateOnlyOneDevice(
            HttpServletRequest httpServletRequest,
            @RequestBody AuthenticateRequest request
    ) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticateOnlyOneDevice(request, httpServletRequest);
        String accessToken = authenticationResponse.getAccessToken();
        // get role from token
        String role = jwtService.getRoleFromToken(accessToken);
        authenticationResponse.setRedirect("/" + role.toLowerCase() + "/dashboard");
        return authenticationResponse;
    }
}
