package com.codeit.project.slid_todo.application.todoManage.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "투두 생성 요청")
public class CreateTodoRequestDto {

    @NotNull
    @Schema(description = "목표 ID", example = "1")
    private Long goalId;

    @NotBlank(message = "투두 내용은 필수입니다.")
    @Schema(description = "투두 내용", example = "스프링 부트 공부하기")
    private String content;

    @Schema(description = "공유 투두 여부 (스터디장만 생성 가능)", example = "false")
    private boolean shared;
} 