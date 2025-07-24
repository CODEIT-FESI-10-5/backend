package com.codeit.project.slid_todo.common.security.errorCode;

import com.codeit.project.slid_todo.common.exception.errorCode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_001", " Access Token 만료"),
    INVALID_SIGNATURE_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED,  "AUTH_002","서명이 올바르지 않습니다"),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "AUTH_003", "접근 권한이 없습니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH_004", "아이디 또는 비밀번호가 올바르지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH_005", "저장된 RefreshToken이 존재하지 않습니다"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_006", "RefreshToken 기간이 만료 되었습니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
