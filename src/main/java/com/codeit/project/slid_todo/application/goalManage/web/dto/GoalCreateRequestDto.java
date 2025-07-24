package com.codeit.project.slid_todo.application.goalManage.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "목표 생성 요청")
public class GoalCreateRequestDto {
    @JsonProperty("studyId")
    @Schema(description = "스터디 ID", example = "1")
    private Long studyId;
    
    @Schema(description = "목표 제목", example = "스프링 부트 마스터하기")
    private String title;
} 