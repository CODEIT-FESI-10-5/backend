package com.codeit.project.slid_todo.common.security.handler;

import com.codeit.project.slid_todo.application.auth.business.AuthFacade;
import com.codeit.project.slid_todo.common.dto.ResponseDto;
import com.codeit.project.slid_todo.common.security.dto.LoginDto;
import com.codeit.project.slid_todo.common.security.dto.RefreshTokenDto;
import com.codeit.project.slid_todo.common.security.jwt.JwtProvider;
import com.codeit.project.slid_todo.common.security.vo.CustomUserDetails;
import com.codeit.project.slid_todo.common.util.CookieUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;
    private final CookieUtils cookieUtils;

    @Lazy
    private final AuthFacade authFacade;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserIdx();
        String email = userDetails.getUsername();

        String accessToken = jwtProvider.generateAccessToken(email, userId);
        String refreshToken = jwtProvider.generateRefreshToken(email);
        Date refreshTokenExpiry = jwtProvider.getClaims(refreshToken).getExpiration();

        authFacade.registerRefreshToken(new RefreshTokenDto(userId, refreshToken, refreshTokenExpiry));

        addTokensToResponse(response, accessToken, refreshToken);

        LoginDto.Response responseDate = LoginDto.Response.from(userDetails);

        ResponseDto<LoginDto.Response> responseDto = ResponseDto.<LoginDto.Response>builder()
                .httpStatusCode(HttpServletResponse.SC_OK)
                .data(responseDate)
                .build();

        writeJsonResponse(response, responseDto);
    }

    private void addTokensToResponse(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
//        response.addHeader(HttpHeaders.AUTHORIZATION, jwtProperties.getTokenPrefix() + accessToken);

        Cookie accessTokenCookie = cookieUtils.createAccessTokenCookie(accessToken);
        Cookie refreshTokenCookie = cookieUtils.createRefreshTokenCookie(refreshToken);
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    private void writeJsonResponse(HttpServletResponse response, ResponseDto<LoginDto.Response> body) throws IOException {
        setDefaultJsonResponseHeader(response, body);
        try (OutputStream out = response.getOutputStream()) {
            objectMapper.writeValue(out, body);
        }
    }

    private void setDefaultJsonResponseHeader(HttpServletResponse response, ResponseDto<LoginDto.Response> body) {
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(body.getHttpStatusCode());
    }

}
