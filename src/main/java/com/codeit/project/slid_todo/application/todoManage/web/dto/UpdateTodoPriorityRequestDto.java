package com.codeit.project.slid_todo.application.todoManage.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Getter
@Schema(description = "투두 우선순위 수정 요청")
public class UpdateTodoPriorityRequestDto {

    @NotNull(message = "투두 ID는 필수입니다.")
    @Schema(description = "투두 ID", example = "1")
    private Long todoId;

    @NotNull(message = "우선순위는 필수입니다.")
    @Min(value = 1, message = "우선순위는 1 이상이어야 합니다.")
    @Max(value = 10, message = "우선순위는 10 이하여야 합니다.")
    @Schema(description = "새로운 우선순위 (1-10)", example = "4")
    private Integer priorityOrder;
} 