package com.nixagh.classicmodels.service.OAuth2User;

import com.nixagh.classicmodels.entity.user.LoginType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Collection;
import java.util.Map;

public class OAuth2FacebookUser extends OAuth2UserDetail{
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
