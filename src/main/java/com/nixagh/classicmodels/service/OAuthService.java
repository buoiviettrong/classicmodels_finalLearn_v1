package com.nixagh.classicmodels.service;

import com.nixagh.classicmodels._common.auth.AuthenticateRequest;
import com.nixagh.classicmodels._common.auth.AuthenticationResponse;
import com.nixagh.classicmodels._common.auth.AuthenticationService;
import com.nixagh.classicmodels._common.auth.RegisterRequest;
import com.nixagh.classicmodels.entity.user.LoginType;
import com.nixagh.classicmodels.entity.user.Role;
import com.nixagh.classicmodels.entity.user.User;
import com.nixagh.classicmodels.repository.TokenRepository;
import com.nixagh.classicmodels.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {
  private final AuthenticationService authenticationService;
  private final UserRepository userRepository;
  public AuthenticationResponse generationToken(
      Authentication authentication,
      HttpServletResponse response,
      HttpServletRequest request
  ) {
    AuthenticationResponse authenticationResponse;

    DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
    String name = defaultOAuth2User.getAttribute("name");
    String email = defaultOAuth2User.getAttribute("email") == null
        ? defaultOAuth2User.getAttribute("login") + "@gmail.com"
        : defaultOAuth2User.getAttribute("email");
    LoginType loginType = defaultOAuth2User.getAttribute("email") == null
        ? LoginType.GITHUB
        : LoginType.GOOGLE;
    User user = userRepository.getUserByEmail(email).orElse(null);
    if (user == null)
      authenticationResponse = authenticationService.register(
          RegisterRequest.builder()
              .email(email)
              .password(email)
              .firstName(name)
              .lastName(name)
              .role(Role.USER)
              .type(loginType)
              .build()
      );
    else
      authenticationResponse = authenticationService.authenticate(
          AuthenticateRequest.builder()
              .email(email)
              .password(email)
              .build()
      );
    // clear session, cookies
    clearCookie(request);
    return authenticationResponse;
  }

  private void clearCookie(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    for (Cookie cookie : request.getCookies()) {
      cookie.setMaxAge(0);
    }
    SecurityContextHolder.clearContext();
  }
}
