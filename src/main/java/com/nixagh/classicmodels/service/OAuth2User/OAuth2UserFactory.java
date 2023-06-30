package com.nixagh.classicmodels.service.OAuth2User;

import com.nixagh.classicmodels.entity.enums.Provider;
import com.nixagh.classicmodels.exception.NotFoundEntity;

import java.util.Map;

public class OAuth2UserFactory {
    public static OAuth2UserDetail getOauth2UserDetail(String registrationId, Map<String, Object> attributes) {
       if (registrationId.equals(Provider.github.name()))
           return new OAuth2GitHubUser(attributes);
       else if (registrationId.equals(Provider.facebook.name()))
           return new OAuth2FacebookUser(attributes);
       else if (registrationId.equals(Provider.google.name()))
           return new OAuth2GoogleUser(attributes);
       throw new NotFoundEntity("Wrong registration id: " + registrationId);
    }
}
