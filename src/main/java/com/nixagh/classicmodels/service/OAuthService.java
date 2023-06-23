package com.nixagh.classicmodels.service;

import com.nixagh.classicmodels._common.auth.AuthenticateRequest;
import com.nixagh.classicmodels._common.auth.AuthenticationResponse;
import com.nixagh.classicmodels._common.auth.AuthenticationService;
import com.nixagh.classicmodels._common.auth.RegisterRequest;
import com.nixagh.classicmodels.entity.token.Token;
import com.nixagh.classicmodels.entity.user.LoginType;
import com.nixagh.classicmodels.entity.user.Role;
import com.nixagh.classicmodels.entity.user.User;
import com.nixagh.classicmodels.repository.TokenRepository;
import com.nixagh.classicmodels.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {
  private final AuthenticationService authenticationService;
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthenticationResponse generationToken(Authentication authentication) {
    AuthenticationResponse authenticationResponse;

    DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
    String name =  defaultOAuth2User.getAttribute("name");
    String email = defaultOAuth2User.getAttribute("email") == null
        ? defaultOAuth2User.getAttribute("login") + "@gmail.com"
        : defaultOAuth2User.getAttribute("email");
    LoginType loginType = defaultOAuth2User.getAttribute("email") == null
        ? LoginType.GITHUB
        :  LoginType.GOOGLE;
    User user = userRepository.getUserByEmail(email).orElse(null);
    System.out.println(email);
    if(user == null)
      authenticationResponse = authenticationService.register(
        RegisterRequest.builder()
            .email(email)
            .password(passwordEncoder.encode("password"))
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
              .password("password")
              .build()
      );

    return authenticationResponse;
  }
}
