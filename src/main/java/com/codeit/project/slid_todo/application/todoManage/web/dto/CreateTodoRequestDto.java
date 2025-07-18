package com.codeit.project.slid_todo.application.todoManage.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateTodoRequestDto {

    @NotNull
    private Long goalId;

    @NotBlank(message = "투두 내용은 필수입니다.")
    private String content;

    private boolean shared;
} 