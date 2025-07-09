package com.codeit.project.slid_todo.common.security.handler;

import com.codeit.project.slid_todo.common.dto.ResponseDto;
import com.codeit.project.slid_todo.common.security.errorCode.AuthErrorCode;
import com.codeit.project.slid_todo.common.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ResponseUtil responseUtil;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .httpStatusCode(HttpServletResponse.SC_OK)
                .errorCode(AuthErrorCode.UNAUTHENTICATED.getCode())
                .errorMessage(AuthErrorCode.UNAUTHENTICATED.getMessage())
                .build();

        responseUtil.writeJsonResponse(response, responseDto);
    }

}
