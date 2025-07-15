package com.codeit.project.slid_todo.application.noteManage.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class NoteListResponseDto {
    private int totalCount;
    private List<NoteData> notes;

    @Getter
    @Builder
    public static class NoteData {
        private Long id;
        private String title;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
} 