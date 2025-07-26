package com.codeit.project.slid_todo.application.goalManage.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "목표 응답")
public class GoalResponseDto {
    @Schema(description = "목표 ID", example = "1")
    private Long id;
    
    @Schema(description = "목표 제목", example = "스프링 부트 마스터하기")
    private String title;
} 