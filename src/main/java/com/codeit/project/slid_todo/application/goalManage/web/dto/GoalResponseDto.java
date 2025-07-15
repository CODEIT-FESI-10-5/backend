package com.codeit.project.slid_todo.application.goalManage.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoalResponseDto {
    private Long id;
    private String title;
    private String priorityOrder;
} 