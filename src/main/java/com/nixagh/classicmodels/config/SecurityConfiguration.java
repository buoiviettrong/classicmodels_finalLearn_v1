package com.nixagh.classicmodels.config;

import com.nixagh.classicmodels.service.OAuth2User.OAuthUserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAuthorizationEntryPoint customAuthorizationEntryPoint;

    DefaultOAuth2UserService oauth2Delegate = new DefaultOAuth2UserService();
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(
                                "/api/v1/employee/**",
                                "/api/v1/auth/**",
                                "/api/v1/oauth/**",
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html",
                                "/login/**",
                                "/swagger-ui/index.html#/",
                                "/favicon.ico/**",
                                "/static/**"
                        )
                        .permitAll()
                        .requestMatchers("/api/v1/management/**").hasAnyRole("ADMIN", "MANAGER")
                        .anyRequest()
                        .authenticated()
                )
                .oauth2Login(oAuth2LoginConfigurer ->
                        oAuth2LoginConfigurer
                                .loginPage("/login")
                                .userInfoEndpoint(userInfoEndpointConfigurer ->
                                        userInfoEndpointConfigurer
                                                .userService(userRequest -> {
                                                    OAuth2User oAuth2User = oauth2Delegate.loadUser(userRequest);
                                                    return OAuthUserFactory.getOauth2UserDetail(
                                                            userRequest.getClientRegistration().getRegistrationId().toUpperCase(),
                                                            oAuth2User.getAttributes());
                                                })
                                                .oidcUserService(userRequest -> {
                                                    OAuth2User oAuth2User = oauth2Delegate.loadUser(userRequest);
                                                    return OAuthUserFactory.getOauth2UserDetail(
                                                            userRequest.getClientRegistration().getRegistrationId().toUpperCase(),
                                                            oAuth2User.getAttributes());
                                                })
                                )
//                                .successHandler(successHandler)
                                .defaultSuccessUrl("/api/v1/oauth2/success")
                )
                .authenticationProvider(authenticationProvider)
                .addFilterAfter(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAuthorizationEntryPoint)
                )
        ;
        httpSecurity.logout(logout -> logout
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
        );
        return httpSecurity.build();
    }
}
