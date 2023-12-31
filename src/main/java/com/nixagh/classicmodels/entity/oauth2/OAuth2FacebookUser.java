package com.nixagh.classicmodels.entity.oauth2;

import com.nixagh.classicmodels.entity.enums.LoginType;

import java.util.Map;

public class OAuth2FacebookUser extends OAuth2UserDetail {
    public OAuth2FacebookUser(Map<String, Object> attributes) {
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
        return LoginType.FACEBOOK;
    }


}
