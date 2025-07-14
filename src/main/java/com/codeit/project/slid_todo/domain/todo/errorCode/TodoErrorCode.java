package com.codeit.project.slid_todo.domain.todo.errorCode;

import com.codeit.project.slid_todo.common.exception.errorCode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TodoErrorCode implements ErrorCode {

    NOT_EXIST_TODO(HttpStatus.NOT_FOUND, "TODO_001", "존재하지 않는 투두입니다."),
    TODO_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "TODO_002", "투두 생성 한도를 초과했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
} 