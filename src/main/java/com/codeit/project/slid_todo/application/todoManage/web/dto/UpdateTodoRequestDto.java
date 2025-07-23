package com.codeit.project.slid_todo.application.todoManage.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "투두 수정 요청")
public class UpdateTodoRequestDto {

    @NotNull(message = "목표 아이디는 필수입니다.")
    @Schema(description = "목표 ID", example = "1")
    private Long goalId;

    @NotBlank(message = "투두 내용은 필수입니다.")
    @Schema(description = "투두 내용", example = "스프링 부트 공부하기 - 완료")
    private String content;

    @Schema(description = "완료 여부", example = "true")
    private boolean completed;
} 