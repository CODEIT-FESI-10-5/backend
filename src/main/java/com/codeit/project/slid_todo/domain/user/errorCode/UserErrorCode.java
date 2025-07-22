package com.codeit.project.slid_todo.domain.user.errorCode;

import com.codeit.project.slid_todo.common.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "", "이미 존재하는 이메일입니다."),
    NOT_EXIST_USER(HttpStatus.NOT_FOUND, "", "해당 사용자가 존재하지 않습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "", "현재 비밀번호가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() { return httpStatus; }

    @Override
    public String getCode() { return code; }

    @Override
    public String getMessage() { return message; }
}
