package com.codeit.project.slid_todo.application.goalManage.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "목표 수정 요청")
public class GoalUpdateRequestDto {
    @Schema(description = "목표 제목", example = "스프링 부트 완전 마스터하기")
    private String title;
} 