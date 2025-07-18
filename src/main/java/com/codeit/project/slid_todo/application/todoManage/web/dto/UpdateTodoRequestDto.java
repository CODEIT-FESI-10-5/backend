package com.codeit.project.slid_todo.application.todoManage.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateTodoRequestDto {

    @NotNull(message = "목표 아이디는 필수입니다.")
    private Long goalId;

    @NotBlank(message = "투두 내용은 필수입니다.")
    private String content;

    private boolean completed;
} 