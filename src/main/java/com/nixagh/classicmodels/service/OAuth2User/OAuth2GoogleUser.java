package com.nixagh.classicmodels.service.OAuth2User;

import com.nixagh.classicmodels.entity.user.LoginType;

import java.util.Map;

public class OAuth2GoogleUser extends OAuth2UserDetail {
    public OAuth2GoogleUser(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public LoginType getLoginType() {
        return LoginType.GOOGLE;
    }
}
