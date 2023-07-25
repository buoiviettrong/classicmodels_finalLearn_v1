package com.nixagh.classicmodels.utils.oauth2;

import com.nixagh.classicmodels.entity.oauth2.OAuth2GitHubUser;
import com.nixagh.classicmodels.entity.oauth2.OAuth2GoogleUser;
import com.nixagh.classicmodels.entity.oauth2.OAuth2UserDetail;

import java.util.Map;

public class OAuthUserUtil {
    public static OAuth2UserDetail getOauth2UserDetail(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "GITHUB" -> new OAuth2GitHubUser(attributes);
            case "GOOGLE" -> new OAuth2GoogleUser(attributes);
            default -> throw new RuntimeException("Not found registrationId");
        };
    }
}
