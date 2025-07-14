package com.codeit.project.slid_todo.application.todoManage.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateTodoRequestDto {

    @NotBlank(message = "투두 내용은 필수입니다.")
    private String content;

    private boolean completed;
} 