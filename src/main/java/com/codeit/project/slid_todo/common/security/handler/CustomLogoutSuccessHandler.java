package com.codeit.project.slid_todo.common.security.handler;

import com.codeit.project.slid_todo.common.dto.ResponseDto;
import com.codeit.project.slid_todo.common.util.CookieUtils;
import com.codeit.project.slid_todo.common.util.ResponseUtil;
import com.codeit.project.slid_todo.domain.refreshToken.business.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final ResponseUtil responseUtil;
    private final CookieUtils cookieUtils;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        invalidateRefreshToken(request, response);

        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .httpStatusCode(HttpServletResponse.SC_OK)
                .build();

        responseUtil.writeJsonResponse(response, responseDto);
    }

    private void invalidateRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieUtils.extractRefreshToken(request);

        if (!StringUtils.hasText(refreshToken)) {
            return;
        }

        refreshTokenService.delete(refreshToken);
        response.addCookie(cookieUtils.createDeleteCookie("accessToken"));
        response.addCookie(cookieUtils.createDeleteCookie("refreshToken"));
    }
}
