package com.nixagh.classicmodels.utils.oauth2;

import com.nixagh.classicmodels.entity.oauth2.OAuth2GitHubUser;
import com.nixagh.classicmodels.entity.oauth2.OAuth2GoogleUser;
import com.nixagh.classicmodels.entity.oauth2.OAuth2UserDetail;

import java.util.Map;

public class OAuth2UserUtil {
    public static OAuth2UserDetail getOauth2UserDetail(String registrationId, Map<String, Object> attributes) {
        return registrationId == null ? new OAuth2GitHubUser(attributes) : new OAuth2GoogleUser(attributes);
    }
}
