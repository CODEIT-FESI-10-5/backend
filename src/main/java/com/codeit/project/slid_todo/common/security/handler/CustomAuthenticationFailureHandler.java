package com.codeit.project.slid_todo.common.security.handler;

import com.codeit.project.slid_todo.common.dto.ResponseDto;
import com.codeit.project.slid_todo.common.security.errorCode.AuthErrorCode;
import com.codeit.project.slid_todo.common.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ResponseUtil responseUtil;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .httpStatusCode(HttpServletResponse.SC_BAD_REQUEST)
                .errorCode(AuthErrorCode.AUTHENTICATION_FAILED.getCode())
                .errorMessage(AuthErrorCode.AUTHENTICATION_FAILED.getMessage())
                .build();

        responseUtil.writeJsonResponse(response, responseDto);
    }

}
