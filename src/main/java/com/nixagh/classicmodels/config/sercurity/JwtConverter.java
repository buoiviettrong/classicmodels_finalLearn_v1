package com.nixagh.classicmodels.config.sercurity;

import com.nixagh.classicmodels.entity.auth.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class JwtConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {
    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
        User user = new User();
        user.setEmail(jwt.getSubject());
        return new UsernamePasswordAuthenticationToken(user, jwt, new HashSet<>());
    }
}
