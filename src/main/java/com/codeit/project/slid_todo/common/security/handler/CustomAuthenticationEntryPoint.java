package com.codeit.project.slid_todo.common.security.handler;

import com.codeit.project.slid_todo.common.dto.ResponseDto;
import com.codeit.project.slid_todo.common.security.errorCode.AuthErrorCode;
import com.codeit.project.slid_todo.common.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ResponseUtil responseUtil;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        AuthErrorCode errorCode = getErrorCodeByRequest(request);

        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .httpStatusCode(HttpServletResponse.SC_OK)
                .errorCode(errorCode.getCode())
                .errorMessage(errorCode.getMessage())
                .build();

        responseUtil.writeJsonResponse(response, responseDto);
    }

    private AuthErrorCode getErrorCodeByRequest(HttpServletRequest request) {
        if (!(request.getAttribute("errorCode") instanceof AuthErrorCode)) {
            return AuthErrorCode.UNAUTHENTICATED;
        }
        return (AuthErrorCode) request.getAttribute("errorCode");
    }
}
