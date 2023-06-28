package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels._common.auth.AuthenticationResponse;
import com.nixagh.classicmodels.service.OAuthService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Hidden
public class OAuthController {

  private final OAuthService oAuthService;

  @GetMapping("/api/v1/oauth")
  public AuthenticationResponse getToken(
      Authentication authentication,
      HttpServletResponse response,
      HttpServletRequest request
  ) {
    return oAuthService.generationToken(authentication, response, request);
  }
}
