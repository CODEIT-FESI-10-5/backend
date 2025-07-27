package com.codeit.project.slid_todo.common.security.filter;

import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.common.exception.errorCode.CommonErrorCode;
import com.codeit.project.slid_todo.common.security.dto.LoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginDto.Request loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.Request.class);
            UsernamePasswordAuthenticationToken authenticationToken = createAuthenticationToken(loginDto);
            return getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new BaseException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private UsernamePasswordAuthenticationToken createAuthenticationToken(LoginDto.Request loginDto) {
        String email = loginDto.email();
        String password = loginDto.password();
        return new UsernamePasswordAuthenticationToken(email, password);
    }

}
