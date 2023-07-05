package com.nixagh.classicmodels.service.OAuth2User;

import com.nixagh.classicmodels.entity.enums.Provider;
import com.nixagh.classicmodels.exception.NotFoundEntity;

import java.util.Map;

public class OAuth2UserFactory {
    public static OAuth2UserDetail getOauth2UserDetail(String registrationId, Map<String, Object> attributes) {
       return registrationId == null ? new OAuth2GitHubUser(attributes) : new OAuth2GoogleUser(attributes);
    }
}
