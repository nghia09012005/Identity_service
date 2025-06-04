package com.example.identity_service.Configuration;

import com.example.identity_service.Exception.error;
import com.example.identity_service.dto.request.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        error errorcode = error.UNAUTHENTICATED;

        //set status va content type cho http servlet response
        response.setStatus(errorcode.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // set content thanh json

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorcode.getCode())
                .message(errorcode.getMessage())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse)); // ghi api response vao response cua servlet
        response.flushBuffer();

    }
}
