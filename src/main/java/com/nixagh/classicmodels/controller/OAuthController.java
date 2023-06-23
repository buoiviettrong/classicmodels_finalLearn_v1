package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels._common.auth.AuthenticationResponse;
import com.nixagh.classicmodels.service.OAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuthController {

  private final OAuthService oAuthService;

  @GetMapping("/api/v1/oauth")
  public AuthenticationResponse getToken(Authentication authentication) {
    return oAuthService.generationToken(authentication);
  }

}
