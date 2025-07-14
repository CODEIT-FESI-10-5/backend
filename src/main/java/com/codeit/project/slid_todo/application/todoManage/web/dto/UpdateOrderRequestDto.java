package com.codeit.project.slid_todo.application.todoManage.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateOrderRequestDto {

    @NotEmpty(message = "순서 정보는 필수입니다.")
    private List<String> order;
} 