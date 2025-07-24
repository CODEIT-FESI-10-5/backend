package com.codeit.project.slid_todo.domain.studyUser.errorCode;

import com.codeit.project.slid_todo.common.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum StudyUserErrorCode implements ErrorCode {

    STUDY_USER_NOT_FOUND(HttpStatus.FORBIDDEN, "STUDY_USER_001", "해당 스터디에 참여하고 있지 않습니다."),
    NOT_STUDY_LEADER(HttpStatus.FORBIDDEN, "STUDY_USER_002", "스터디 방장만 수행할 수 있는 작업입니다."),
    ALREADY_JOINED(HttpStatus.CONFLICT, "STUDY_USER_003", "이미 해당 스터디에 참여하셨습니다.");

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
