package com.codeit.project.slid_todo.common.security.handler;

import com.codeit.project.slid_todo.application.auth.business.AuthFacade;
import com.codeit.project.slid_todo.common.dto.ResponseDto;
import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.common.security.dto.RefreshTokenDto;
import com.codeit.project.slid_todo.common.security.errorCode.AuthErrorCode;
import com.codeit.project.slid_todo.common.security.jwt.JwtProperties;
import com.codeit.project.slid_todo.common.security.jwt.JwtProvider;
import com.codeit.project.slid_todo.common.security.vo.CustomUserDetails;
import com.codeit.project.slid_todo.common.util.CookieUtils;
import com.codeit.project.slid_todo.common.util.ResponseUtil;
import com.codeit.project.slid_todo.domain.user.persistent.entity.enums.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ResponseUtil responseUtil;
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

        UserRole userRole = extractUserRole(authentication.getAuthorities());

        String accessToken = jwtProvider.generateAccessToken(email, userId, userRole);
        String refreshToken = jwtProvider.generateRefreshToken(email);
        Date refreshTokenExpiry = jwtProvider.getClaims(refreshToken).getExpiration();

        authFacade.registerRefreshToken(new RefreshTokenDto(userId, refreshToken, refreshTokenExpiry));

        addTokensToResponse(response, accessToken, refreshToken);

        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .httpStatusCode(HttpServletResponse.SC_OK)
                .build();

        responseUtil.writeJsonResponse(response, responseDto);
    }

    private UserRole extractUserRole(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(UserRole::fromRole)
                .orElseThrow(() -> new BaseException(AuthErrorCode.UNAUTHENTICATED));
    }

    private void addTokensToResponse(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.AUTHORIZATION, JwtProperties.TOKEN_PREFIX + accessToken);

        Cookie refreshTokenCookie = cookieUtils.createCookie("refreshToken", refreshToken);
        response.addCookie(refreshTokenCookie);
    }

}
