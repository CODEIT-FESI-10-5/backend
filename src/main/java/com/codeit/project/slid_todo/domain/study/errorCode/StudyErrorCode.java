package com.codeit.project.slid_todo.domain.study.errorCode;

import com.codeit.project.slid_todo.common.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum StudyErrorCode implements ErrorCode {
    
    NOT_EXIST_STUDY(HttpStatus.NOT_FOUND, "", "해당 스터디가 존재하지 않습니다.");

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
