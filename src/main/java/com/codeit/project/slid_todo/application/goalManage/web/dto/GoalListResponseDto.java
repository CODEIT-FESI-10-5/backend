package com.codeit.project.slid_todo.application.goalManage.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "목표 목록 응답")
public class GoalListResponseDto {
    @Schema(description = "스터디 ID", example = "1")
    private Long studyId;
    
    @Schema(description = "목표 목록")
    private List<GoalResponseDto> goals;
    
    @Schema(description = "목표 개수", example = "5")
    private int totalCount;
} 