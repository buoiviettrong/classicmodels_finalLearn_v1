package com.nixagh.classicmodels.service;

import com.nixagh.classicmodels._common.auth.AuthenticateRequest;
import com.nixagh.classicmodels._common.auth.AuthenticationResponse;
import com.nixagh.classicmodels._common.auth.AuthenticationService;
import com.nixagh.classicmodels._common.auth.RegisterRequest;
import com.nixagh.classicmodels.entity.user.LoginType;
import com.nixagh.classicmodels.entity.user.User;
import com.nixagh.classicmodels.repository.authRepo.RoleRepository;
import com.nixagh.classicmodels.repository.authRepo.UserRepository;
import com.nixagh.classicmodels.service.OAuth2User.OAuth2UserDetail;
import com.nixagh.classicmodels.service.OAuth2User.OAuth2UserFactory;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService extends DefaultOAuth2UserService {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        try {
            return null;
        } catch (OAuth2AuthenticationException e) {
            return null;
        }

    }

    public AuthenticationResponse generationToken(
            Authentication authentication,
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

        UserDetails user = userRepository.getUserByEmail(email).orElse(null);

        if (user == null)
            authenticationResponse = authenticationService.register(
                    RegisterRequest.builder()
                            .email(email)
                            .password(email)
                            .firstName(name)
                            .lastName(name)
                            .role(roleRepository.getRoleByRoleName("USER"))
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
