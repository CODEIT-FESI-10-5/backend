package com.codeit.project.slid_todo.domain.study.errorCode;

import com.codeit.project.slid_todo.common.exception.errorCode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StudyErrorCode implements ErrorCode {

    NOT_EXIST_STUDY(HttpStatus.NOT_FOUND, "STUDY_001", "존재하지 않는 스터디입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
} 