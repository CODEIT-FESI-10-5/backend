package com.codeit.project.slid_todo.domain.goal.errorCode;

import com.codeit.project.slid_todo.common.exception.errorCode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GoalErrorCode implements ErrorCode {

    NOT_EXIST_GOAL(HttpStatus.NOT_FOUND, "GOAL_001", "존재하지 않는 목표입니다."),
    GOAL_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "GOAL_002", "목표 생성 한도를 초과했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
} 