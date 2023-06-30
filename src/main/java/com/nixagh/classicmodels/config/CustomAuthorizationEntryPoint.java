package com.nixagh.classicmodels.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nixagh.classicmodels.exception.AccessDenied;
import com.nixagh.classicmodels.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.rmi.AccessException;

public class CustomAuthorizationEntryPoint implements AccessDeniedHandler {
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {

        JSONObject json = new JSONObject();
        json.put("status", HttpStatus.UNAUTHORIZED);
        json.put("message", "Access denied");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(json.toJSONString());
    }
}
