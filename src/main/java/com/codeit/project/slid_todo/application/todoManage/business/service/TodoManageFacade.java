package com.codeit.project.slid_todo.application.todoManage.business.service;

import com.codeit.project.slid_todo.application.todoManage.web.dto.*;
import com.codeit.project.slid_todo.domain.goal.business.service.GoalService;
import com.codeit.project.slid_todo.domain.goal.persistent.entity.Goal;
import com.codeit.project.slid_todo.domain.note.business.service.NoteService;
import com.codeit.project.slid_todo.domain.studyUser.business.service.StudyUserService;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.enums.UserRole;
import com.codeit.project.slid_todo.domain.todo.business.service.TodoService;
import com.codeit.project.slid_todo.domain.todo.persistent.entity.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoManageFacade {

    private final GoalService goalService;
    private final TodoService todoService;
    private final NoteService noteService;
    private final StudyUserService studyUserService;

    public TodoListResponseDto getTodoList(Long goalId, Long userId) {
        Goal goal = goalService.getGoalById(goalId);
        
        // 사용자의 투두 목록 조회 (우선순위 순서대로)
        List<Todo> userTodos = todoService.getTodosByGoalIdAndUserId(goalId, userId);
        
        // 우선순위 순서대로 정렬
        List<Todo> sortedTodos = sortTodosByPriority(userTodos, goal.getPriorityOrder());
        
        // 투두 데이터 생성 (N+1 문제 개선: 이미 Fetch Join으로 Note 정보를 가져옴)
        List<TodoListResponseDto.TodoData> myTodoList = sortedTodos.stream()
                .map(todo -> {
                    String noteContent = "";
                    if (todo.getNote() != null) {
                        noteContent = todo.getNote().getContent();
                    }
                    
                    return TodoListResponseDto.TodoData.builder()
                            .todoId(todo.getId().toString())
                            .content(todo.getContent())
                            .createdAt(todo.getCreatedAt())
                            .completed(todo.isCompleted())
                            .completedAt(todo.getCompletedAt())
                            .note(noteContent)
                            .shared(todo.isShared())
                            .build();
                })
                .collect(Collectors.toList());
        
        // 순서 정보 생성
        List<String> order = sortedTodos.stream()
                .map(todo -> todo.getId().toString())
                .collect(Collectors.toList());
        
        return TodoListResponseDto.builder()
                .myTodoList(myTodoList)
                .order(order)
                .build();
    }

    private List<Todo> sortTodosByPriority(List<Todo> todos, String priorityOrder) {
        if (priorityOrder == null || priorityOrder.isEmpty()) {
            // 우선순위가 없으면 생성일 순으로 정렬
            return todos.stream()
                    .sorted((t1, t2) -> t1.getCreatedAt().compareTo(t2.getCreatedAt()))
                    .collect(Collectors.toList());
        }
        
        // 우선순위 순서대로 정렬
        List<String> priorityIds = Arrays.asList(priorityOrder.split(","));
        
        return todos.stream()
                .sorted((t1, t2) -> {
                    int index1 = priorityIds.indexOf(t1.getId().toString());
                    int index2 = priorityIds.indexOf(t2.getId().toString());
                    
                    if (index1 == -1 && index2 == -1) {
                        return t1.getCreatedAt().compareTo(t2.getCreatedAt());
                    } else if (index1 == -1) {
                        return 1;
                    } else if (index2 == -1) {
                        return -1;
                    } else {
                        return Integer.compare(index1, index2);
                    }
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void createTodo(Long userId, CreateTodoRequestDto requestDto) {
        Long goalId = requestDto.getGoalId();
        Goal goal = goalService.getGoalById(goalId);
        StudyUser currentUser = studyUserService.getOrThrowIfNotJoined(goal.getStudy().getId(), userId);
        
        if (requestDto.isShared()) {
            // 공통 투두는 스터디장만 생성 가능
            if (currentUser.getUserRole() != UserRole.LEADER) {
                throw new RuntimeException("공통 투두는 스터디장만 생성할 수 있습니다.");
            }
            todoService.createSharedTodosForAllMembers(goalId, requestDto.getContent());
        } else {
            // 개인 투두 생성
            todoService.createTodo(goalId, currentUser.getId(), requestDto.getContent(), false);
        }
    }

    @Transactional
    public void updateTodo(Long todoId, Long userId, UpdateTodoRequestDto requestDto) {
        Long goalId = requestDto.getGoalId();
        Goal goal = goalService.getGoalById(goalId);
        StudyUser currentUser = studyUserService.getOrThrowIfNotJoined(goal.getStudy().getId(), userId);
        
        // 투두가 현재 사용자의 것인지 확인
        Todo todo = todoService.getTodoById(todoId);
        if (!todo.getAssignedUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("자신의 투두만 수정할 수 있습니다.");
        }
        
        todoService.updateTodoContentAndCompleted(todoId, requestDto.getContent(), requestDto.isCompleted());
    }

} 