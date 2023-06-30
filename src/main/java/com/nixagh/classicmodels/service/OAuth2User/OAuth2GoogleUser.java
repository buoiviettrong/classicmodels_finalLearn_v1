package com.nixagh.classicmodels.service.OAuth2User;

import java.util.Map;

public class OAuth2GoogleUser extends OAuth2UserDetail {
    public OAuth2GoogleUser(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    protected String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}
