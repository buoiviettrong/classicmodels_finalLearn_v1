package com.nixagh.classicmodels.utils.jwt;

import com.nixagh.classicmodels.config.sercurity.JwtService;
import com.nixagh.classicmodels.entity.auth.User;
import com.nixagh.classicmodels.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public User getUserFromHeader(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer "))
            return null;
        token = token.replace("Bearer ", "");
        String username = jwtService.getUsernameFromToken(token);
        return userRepository.getUserByEmail(username).orElse(null);
    }
}
