package com.codeit.project.slid_todo.application.todoManage.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateOrderRequestDto {

    @NotNull(message = "목표 아이디는 필수입니다.")
    private Long goalId;

    @NotEmpty(message = "순서 정보는 필수입니다.")
    private List<String> order;
} 