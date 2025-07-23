package com.codeit.project.slid_todo.application.noteManage.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "노트 생성 요청")
public class CreateNoteRequestDto {
    @Schema(description = "투두 ID", example = "1")
    private Long todoId;
    
    @NotBlank(message = "노트 제목은 필수입니다.")
    @Schema(description = "노트 제목", example = "스프링 부트 학습 노트")
    private String title;
    
    @Schema(description = "노트 내용", example = "스프링 부트 기초부터 심화까지 학습한 내용을 정리합니다.")
    private String content;
} 