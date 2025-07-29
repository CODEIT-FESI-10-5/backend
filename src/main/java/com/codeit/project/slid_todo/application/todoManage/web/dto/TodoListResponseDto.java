package com.codeit.project.slid_todo.application.todoManage.web.dto;

import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "투두 목록 조회 응답")
public class TodoListResponseDto {

    @Schema(description = "투두 목록")
    private List<TodoData> myTodoList;

    private String goalTitle;

    private UserRole userRole;

    @Getter
    @Builder
    @Schema(description = "투두 데이터")
    public static class TodoData {
        @Schema(description = "투두 ID", example = "1")
        private String todoId;
        
        @Schema(description = "투두 내용", example = "스프링 부트 공부하기")
        private String content;
        
        @Schema(description = "완료 여부", example = "true")
        private boolean completed;
        
        @Schema(description = "생성일", example = "2024-01-15T10:30:00")
        private LocalDateTime createdAt;
        
        @Schema(description = "완료일", example = "2024-01-16T14:20:00")
        private LocalDateTime completedAt;
        
        @Schema(description = "노트 내용", example = "스프링 부트 기초부터 심화까지 학습")
        private String note;
        
        @Schema(description = "노트 ID", example = "1")
        private String noteId;
        
        @Schema(description = "공유 투두 여부", example = "false")
        private boolean shared;
        
        @Schema(description = "우선순위", example = "1")
        private Integer priorityOrder;
    }
} 