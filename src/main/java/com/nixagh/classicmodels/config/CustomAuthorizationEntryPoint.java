package com.nixagh.classicmodels.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nixagh.classicmodels.exception.AccessDenied;
import com.nixagh.classicmodels.exception.ErrorResponse;
import com.nixagh.classicmodels.exception.NotFoundEntity;
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

        String json = """
                {
                    "status": "%s",
                    "message": "%s"
                }
                """.formatted(HttpStatus.UNAUTHORIZED.getReasonPhrase(), "Access denied");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(json);
    }
}
