package com.codeit.project.slid_todo.common.exception;

import com.codeit.project.slid_todo.common.exception.errorCode.ErrorCode;
import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() { return errorCode.getHttpStatus(); }

    public String getCode() { return errorCode.getCode(); }

    public String getErrorMessage() { return this.getMessage(); }

}
