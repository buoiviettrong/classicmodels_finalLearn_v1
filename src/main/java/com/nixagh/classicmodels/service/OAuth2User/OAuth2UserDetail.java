package com.nixagh.classicmodels.service.OAuth2User;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public abstract class OAuth2UserDetail {
    protected Map<String, Object> attributes;
    public OAuth2UserDetail(Map<String, Object> attributes) {
        this.attributes = attributes;
    };

    protected abstract String getName();
    public abstract String getEmail();
}
