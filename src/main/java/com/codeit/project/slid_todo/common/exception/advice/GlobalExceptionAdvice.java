package com.codeit.project.slid_todo.common.exception.advice;

import com.codeit.project.slid_todo.common.dto.ResponseDto;
import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.common.exception.errorCode.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> BaseException(final BaseException e) {
        var response = ResponseDto.<Void>builder()
                .httpStatusCode(e.getHttpStatus().value())
                .errorCode(e.getCode())
                .errorMessage(e.getMessage())
                .build();

        log.error("[{}] {} {}", e.getClass().getName(), e.getCode(), e.getMessage(), e);

        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> exception(final Exception e) {
        var errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        var response = ResponseDto.<Void>builder()
                .httpStatusCode(errorCode.getHttpStatus().value())
                .errorMessage(e.getMessage())
                .build();

        log.error("[{}] {} {}", e.getClass().getName(), errorCode.getHttpStatus().value(), e.getMessage(), e);

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleValidationExceptions(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        List<ResponseDto.FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
                .map(ResponseDto.FieldError::of)
                .toList();

        var response = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.BAD_REQUEST.value())
                .errorCode("VALIDATION_ERROR")
                .errorMessage("입력값 검증 실패")
                .fieldErrors(fieldErrors)
                .build();

        log.warn("[{}] validation failed: {}", e.getClass().getSimpleName(), fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
