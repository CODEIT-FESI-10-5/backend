package com.codeit.project.slid_todo.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {

    private final int httpStatusCode;
    private final String errorCode;
    private final T data;
    private final String errorMessage;
    private final List<FieldError> fieldErrors;

    @Getter
    @AllArgsConstructor
    public static class FieldError {
        private final String field;
        private final String message;

        public static FieldError of(org.springframework.validation.FieldError error) {
           return new FieldError(error.getField(), error.getDefaultMessage());
        }
    }

    @Builder
    public ResponseDto(int httpStatusCode, String errorCode, T data, String errorMessage, List<FieldError> fieldErrors) {
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.data = data;
        this.errorMessage = errorMessage;
        this.fieldErrors = fieldErrors;
    }
}
