package com.codeit.project.slid_todo.application.noteManage.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "노트 수정 요청")
public class UpdateNoteRequestDto {
    @Schema(description = "노트 내용", example = "스프링 부트 기초부터 심화까지 학습한 내용을 정리하고, 추가로 JPA와 Security도 학습했습니다.")
    private String content;
} 