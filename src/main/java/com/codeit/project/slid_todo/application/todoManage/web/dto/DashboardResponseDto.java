package com.codeit.project.slid_todo.application.todoManage.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "대시보드 조회 응답")
public class DashboardResponseDto {

    @Schema(description = "목표 목록")
    private List<GoalData> goals;

    @Getter
    @Builder
    @Schema(description = "목표 데이터")
    public static class GoalData {
        @Schema(description = "목표 ID", example = "1")
        private String id;
        
        @Schema(description = "목표 제목", example = "스프링 부트 마스터하기")
        private String title;
        
        @Schema(description = "완료 개수 (완료/전체)", example = "4/8")
        private String completedCt;
        
        @Schema(description = "진행률 (%)", example = "50")
        private int progress;
        
        @Schema(description = "최근 완료된 투두")
        private TodoData recentCompletedTodo;
        
        @Schema(description = "진행중인 투두")
        private TodoData inProgressTodo;
        
        @Schema(description = "팀 진행도 목록")
        private List<TeamProgressData> teamProgress;
    }

    @Getter
    @Builder
    @Schema(description = "투두 데이터")
    public static class TodoData {
        @Schema(description = "투두 ID", example = "1")
        private String id;
        
        @Schema(description = "투두 내용", example = "스프링 부트 공부하기")
        private String content;
        
        @Schema(description = "생성일", example = "2024-01-15T10:30:00")
        private String createdAt;
        
        @Schema(description = "완료 여부", example = "true")
        private boolean completed;
        
        @Schema(description = "완료일", example = "2024-01-16T14:20:00")
        private String completedAt;
        
        @Schema(description = "노트 내용", example = "스프링 부트 기초부터 심화까지 학습")
        private String note;
        
        @Schema(description = "노트 ID", example = "1")
        private String noteId;
        
        @Schema(description = "공유 투두 여부", example = "false")
        private boolean shared;
    }

    @Getter
    @Builder
    @Schema(description = "팀 진행도 데이터")
    public static class TeamProgressData {
        @Schema(description = "사용자 이름", example = "김철수")
        private String name;
        
        @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
        private String image;
        
        @Schema(description = "진행률 (%)", example = "75")
        private int progress;
        
        @Schema(description = "완료 개수 (완료/전체)", example = "6/8")
        private String completedCt;
    }
} 