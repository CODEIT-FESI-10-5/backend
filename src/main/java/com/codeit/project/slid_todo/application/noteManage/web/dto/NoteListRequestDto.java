package com.codeit.project.slid_todo.application.noteManage.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "노트 목록 조회 요청")
public class NoteListRequestDto {
    @Schema(description = "목표 ID", example = "1")
    private Long goalId;
    
    @Schema(description = "노트 제목 검색어", example = "스프링")
    private String noteTitle;
} 