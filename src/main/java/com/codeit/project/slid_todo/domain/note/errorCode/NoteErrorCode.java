package com.codeit.project.slid_todo.domain.note.errorCode;

import com.codeit.project.slid_todo.common.exception.errorCode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NoteErrorCode implements ErrorCode {

    NOT_EXIST_NOTE(HttpStatus.NOT_FOUND, "NOTE_001", "존재하지 않는 노트입니다."),
    NOTE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "NOTE_002", "이미 노트가 존재합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
} 