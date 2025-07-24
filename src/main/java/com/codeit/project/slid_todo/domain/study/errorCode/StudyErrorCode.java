package com.codeit.project.slid_todo.domain.study.errorCode;

import com.codeit.project.slid_todo.common.exception.errorCode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StudyErrorCode implements ErrorCode {

    NOT_EXIST_STUDY(HttpStatus.NOT_FOUND, "STUDY_001", "존재하지 않는 스터디입니다."),
    INVALID_INVITE_CODE(HttpStatus.NOT_FOUND, "STUDY_002", "유효하지 않은 초대 코드입니다.");



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