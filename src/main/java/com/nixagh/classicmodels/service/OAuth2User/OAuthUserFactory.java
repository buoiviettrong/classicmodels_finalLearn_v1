package com.nixagh.classicmodels.service.OAuth2User;

import java.util.Map;

public class OAuthUserFactory {
    public static OAuth2UserDetail getOauth2UserDetail(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "GITHUB" -> new OAuth2GitHubUser(attributes);
            case "GOOGLE" -> new OAuth2GoogleUser(attributes);
            default -> throw new RuntimeException("Not found registrationId");
        };
    }
}
