package com.codeit.project.slid_todo.application.goalManage.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class GoalDetailResponseDto {

    private GoalData goal;

    @Getter
    @Builder
    public static class GoalData {
        private String title;
        private String completedCt;
        private List<MyTodoData> mytodoList;
        private List<TeamProgressData> teamProgress;
    }

    @Getter
    @Builder
    public static class MyTodoData {
        private String id;
        private String content;
        private LocalDateTime createdAt;
        private boolean completed;
        private LocalDateTime completedAt;
        private String note;
        private int order;
        private boolean shared;
        private Integer priorityOrder;
    }

    @Getter
    @Builder
    public static class TeamProgressData {
        private String name;
        private String image;
        private int progress;
        private String completedCt;
    }
} 