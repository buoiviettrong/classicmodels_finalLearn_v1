package com.nixagh.classicmodels.config;

import com.nixagh.classicmodels.exception.AccessDenied;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        JSONObject json = new JSONObject();
        json.put("status", HttpStatus.UNAUTHORIZED);
        json.put("message", "Unauthorized");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(json.toJSONString());
    }
}
