package com.example.shopping.config;

import com.example.shopping.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Set;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {


    @Value("${spring.application.un_authorized}")
    private String unauthorizedErrorCode;

    private static final long serialVersionUID = 1L;
    /**
     * It  will check that Authentication will be happened or not based on that it throws an exception
     * if authentication will not be happens
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ErrorResponse errorResponse = new ErrorResponse(unauthorizedErrorCode, Set.of("Unauthorized"));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        OutputStream stream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(stream, errorResponse);
        stream.flush();

    }
}
