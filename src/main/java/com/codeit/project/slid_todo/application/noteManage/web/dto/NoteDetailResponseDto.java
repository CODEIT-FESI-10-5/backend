package com.codeit.project.slid_todo.application.noteManage.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "노트 상세 조회 응답")
public class NoteDetailResponseDto {
    @Schema(description = "노트 ID", example = "1")
    private Long id;
    
    @Schema(description = "노트 제목", example = "스프링 부트 학습 노트")
    private String title;
    
    @Schema(description = "노트 내용", example = "스프링 부트 기초부터 심화까지 학습한 내용을 정리합니다.")
    private String content;
    
    @Schema(description = "생성일", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "수정일", example = "2024-01-16T14:20:00")
    private LocalDateTime updatedAt;
} 