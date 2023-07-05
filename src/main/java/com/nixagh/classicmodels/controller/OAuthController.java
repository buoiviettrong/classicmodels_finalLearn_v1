package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels._common.auth.AuthenticationResponse;
import com.nixagh.classicmodels.service.OAuth2User.OAuth2UserDetail;
import com.nixagh.classicmodels.service.OAuthService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
            HttpServletRequest request,
            @AuthenticationPrincipal OAuth2User principal
    ) throws Exception {
        System.out.println("principal = " + principal.getAuthorities());
        return oAuthService.generationToken(authentication, request);
    }

    @GetMapping("/api/v1/oauth2/success")
    public AuthenticationResponse getAuthenticationResponse(
            HttpServletRequest request,
            @AuthenticationPrincipal OAuth2UserDetail oAuth2UserDetail
    ) {
        System.out.println(oAuth2UserDetail);
        return oAuthService.generationToken(request, oAuth2UserDetail);
    }

}
