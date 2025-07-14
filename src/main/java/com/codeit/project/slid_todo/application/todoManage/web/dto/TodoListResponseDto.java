package com.codeit.project.slid_todo.application.todoManage.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class TodoListResponseDto {

    private List<TodoData> myTodoList;
    private List<String> order;

    @Getter
    @Builder
    public static class TodoData {
        private String todoId;
        private String content;
        private boolean completed;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;
        private String note;
        private boolean shared;
    }
} 