package com.nixagh.classicmodels.service.auth_service;

import com.nixagh.classicmodels.dto.auth.AuthenticateRequest;
import com.nixagh.classicmodels.dto.auth.AuthenticationResponse;
import com.nixagh.classicmodels.dto.auth.RegisterRequest;
import com.nixagh.classicmodels.entity.auth.Role;
import com.nixagh.classicmodels.entity.enums.LoginType;
import com.nixagh.classicmodels.entity.oauth2.OAuth2UserDetail;
import com.nixagh.classicmodels.repository.role.RoleRepository;
import com.nixagh.classicmodels.repository.user.UserRepository;
import com.nixagh.classicmodels.utils.oauth2.OAuth2UserUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OAuthService {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    public AuthenticationResponse generationToken(HttpServletRequest request, OAuth2UserDetail oAuth2UserDetail) {
        AuthenticationResponse authenticationResponse;
        String email = oAuth2UserDetail.getEmail();

        UserDetails user = userRepository.getUserByEmail(email).orElse(null);

        if (user != null)
            authenticationResponse = authenticationService.authenticate(
                    AuthenticateRequest.builder()
                            .email(email)
                            .password(email)
                            .build()
            );
        else {
            String name = oAuth2UserDetail.getName();
            LoginType loginType = oAuth2UserDetail.getLoginType();
            Role role = roleRepository.getRoleByRoleName("MANAGER");

            authenticationResponse = authenticationService.register(
                    RegisterRequest.builder()
                            .email(email)
                            .password(email)
                            .firstName(name)
                            .lastName(name)
                            .role(role)
                            .type(loginType)
                            .build()
            );
        }
        clearCookie(request);
        return authenticationResponse;
    }

    public AuthenticationResponse generationToken(
            Authentication authentication,
            HttpServletRequest request
    ) {
        AuthenticationResponse authenticationResponse;
        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        OAuth2UserDetail oAuth2UserDetail = OAuth2UserUtil.getOauth2UserDetail(
                defaultOAuth2User.getAttribute("email"),
                defaultOAuth2User.getAttributes()
        );

        UserDetails user = userRepository.getUserByEmail(oAuth2UserDetail.getEmail()).orElse(null);

        if (user == null)
            authenticationResponse = authenticationService.register(
                    RegisterRequest.builder()
                            .email(oAuth2UserDetail.getEmail())
                            .password(oAuth2UserDetail.getEmail())
                            .firstName(oAuth2UserDetail.getName())
                            .lastName(oAuth2UserDetail.getName())
                            .role(roleRepository.getRoleByRoleName("MANAGER"))
                            .type(oAuth2UserDetail.getLoginType())
                            .build()
            );
        else
            authenticationResponse = authenticationService.authenticate(
                    AuthenticateRequest.builder()
                            .email(oAuth2UserDetail.getEmail())
                            .password(oAuth2UserDetail.getEmail())
                            .build()
            );
        // clear session, cookies
        clearCookie(request);
        return authenticationResponse;
    }

    private void clearCookie(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        for (Cookie cookie : request.getCookies()) {
            cookie.setMaxAge(0);
        }
        SecurityContextHolder.clearContext();
    }
}
