package com.nixagh.classicmodels.service.auth_service;

import com.nixagh.classicmodels.dto.auth.AuthenticateRequest;
import com.nixagh.classicmodels.dto.auth.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface IAuthenticationService {
    AuthenticationResponse authenticateOnlyOneDevice(AuthenticateRequest request, HttpServletRequest httpServletRequest);
}
