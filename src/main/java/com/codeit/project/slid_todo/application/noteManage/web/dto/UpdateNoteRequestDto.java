package com.codeit.project.slid_todo.application.noteManage.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateNoteRequestDto {
    @NotBlank(message = "노트 제목은 필수입니다.")
    private String title;
    
    private String content;
} 