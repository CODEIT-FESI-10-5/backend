package com.codeit.project.slid_todo.common.exception.errorCode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_001", "서버 내부 에러"),
    INVALID_IMAGE_EXTENSION(HttpStatus.BAD_REQUEST, "COMMON_002", "지원하지 않는 이미지 형식입니다."),
    IMAGE_TOO_LARGE(HttpStatus.BAD_REQUEST, "COMMON_003", "이미지 용량은 5MB를 초과할 수 없습니다."),
    IMAGE_EXTENSION_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMMON_004", "파일에 확장자가 없습니다.");

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
