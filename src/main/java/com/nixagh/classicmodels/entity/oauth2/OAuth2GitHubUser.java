package com.nixagh.classicmodels.entity.oauth2;

import com.nixagh.classicmodels.entity.enums.LoginType;

import java.util.Map;

public class OAuth2GitHubUser extends OAuth2UserDetail {
    public OAuth2GitHubUser(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return attributes.get("login") + "@gmail.com";
    }

    @Override
    public LoginType getLoginType() {
        return LoginType.GITHUB;
    }
}
