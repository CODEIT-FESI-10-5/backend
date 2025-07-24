package com.codeit.project.slid_todo.application.goalManage.business.service;

import com.codeit.project.slid_todo.application.goalManage.web.dto.*;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoalManageFacade {

    private final GoalService goalService;
    private final TodoService todoService;
    private final NoteService noteService;
    private final StudyUserService studyUserService;

    public GoalDetailResponseDto getGoal(Long goalId, Long userId) {
        Goal goal = goalService.getGoalById(goalId);
        
        // 사용자의 투두 목록 조회
        List<Todo> userTodos = todoService.getTodosByGoalIdAndUserId(goalId, userId);
        
        // 우선순위 순서대로 정렬
        List<Todo> sortedTodos = sortTodosByPriority(userTodos, goal.getPriorityOrder());
        
        // 완료된 투두 개수 계산
        long completedCount = todoService.countCompletedByGoalIdAndUserId(goalId, userId);
        long totalCount = todoService.countByGoalIdAndUserId(goalId, userId);
        String completedCt = completedCount + "/" + totalCount;
        
        // 사용자의 투두 데이터 생성 (N+1 문제 개선: 이미 Fetch Join으로 Note 정보를 가져옴)
        List<GoalDetailResponseDto.MyTodoData> myTodoList = sortedTodos.stream()
                .map(todo -> {
                    String noteContent = "";
                    if (todo.getNote() != null) {
                        noteContent = todo.getNote().getContent();
                    }
                    
                    return GoalDetailResponseDto.MyTodoData.builder()
                            .id(todo.getId().toString())
                            .content(todo.getContent())
                            .createdAt(todo.getCreatedAt())
                            .completed(todo.isCompleted())
                            .completedAt(todo.getCompletedAt())
                            .note(noteContent)
                            .order(0) // TODO: 우선순위 로직 구현 필요
                            .shared(todo.isShared())
                            .build();
                })
                .collect(Collectors.toList());
        
        // 팀 진행도 계산
        List<GoalDetailResponseDto.TeamProgressData> teamProgress = calculateTeamProgress(goalId);
        
        return GoalDetailResponseDto.builder()
                .goal(GoalDetailResponseDto.GoalData.builder()
                        .title(goal.getTitle())
                        .completedCt(completedCt)
                        .mytodoList(myTodoList)
                        .teamProgress(teamProgress)
                        .build())
                .build();
    }

    private List<GoalDetailResponseDto.TeamProgressData> calculateTeamProgress(Long goalId) {
        Goal goal = goalService.getGoalById(goalId);
        List<StudyUser> studyUsers = studyUserService.findByStudyId(goal.getStudy().getId());
        
        List<GoalDetailResponseDto.TeamProgressData> teamProgress = new ArrayList<>();
        
        for (StudyUser studyUser : studyUsers) {
            long completedCount = todoService.countCompletedByGoalIdAndUserId(goalId, studyUser.getUser().getId());
            long totalCount = todoService.countByGoalIdAndUserId(goalId, studyUser.getUser().getId());
            
            int progress = totalCount > 0 ? (int) ((completedCount * 100) / totalCount) : 0;
            String completedCt = completedCount + "/" + totalCount;
            
            teamProgress.add(GoalDetailResponseDto.TeamProgressData.builder()
                    .name(studyUser.getUser().getNickname())
                    .image("") // TODO: 사용자 이미지 필드 추가 필요
                    .progress(progress)
                    .completedCt(completedCt)
                    .build());
        }
        
        return teamProgress;
    }

    private List<Todo> sortTodosByPriority(List<Todo> todos, String priorityOrder) {
        if (priorityOrder == null || priorityOrder.isEmpty()) {
            return sortTodosByNewPolicy(todos);
        }

        List<String> priorityIds = Arrays.asList(priorityOrder.split(","));
        
        return todos.stream()
                .sorted((t1, t2) -> {
                    int index1 = priorityIds.indexOf(t1.getId().toString());
                    int index2 = priorityIds.indexOf(t2.getId().toString());
                    
                    if (index1 == -1 && index2 == -1) {
                        // 둘 다 우선순위에 없으면 새로운 정책으로 정렬
                        return sortTodosByNewPolicy(List.of(t1, t2)).get(0).getId().compareTo(sortTodosByNewPolicy(List.of(t1, t2)).get(1).getId());
                    } else if (index1 == -1) {
                        // t1이 우선순위에 없으면 뒤로
                        return 1;
                    } else if (index2 == -1) {
                        // t2가 우선순위에 없으면 뒤로
                        return -1;
                    } else {
                        // 둘 다 우선순위에 있으면 우선순위 오름차순 정렬
                        return Integer.compare(index1, index2);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 새로운 우선순위 정책에 따른 투두 정렬
     * 1. createdAt이 있는 투두들을 createdAt 기준 오름차순 정렬
     * 2. createdAt이 없는 투두들을 기존 priority 기준 내림차순 정렬
     */
    private List<Todo> sortTodosByNewPolicy(List<Todo> todos) {
        return todos.stream()
                .sorted((t1, t2) -> {
                    // createdAt이 있는 투두들을 먼저 정렬
                    boolean t1HasCreatedAt = t1.getCreatedAt() != null;
                    boolean t2HasCreatedAt = t2.getCreatedAt() != null;
                    
                    if (t1HasCreatedAt && t2HasCreatedAt) {
                        // 둘 다 createdAt이 있으면 createdAt 기준 오름차순
                        return t1.getCreatedAt().compareTo(t2.getCreatedAt());
                    } else if (t1HasCreatedAt && !t2HasCreatedAt) {
                        // t1만 createdAt이 있으면 t1이 우선
                        return -1;
                    } else if (!t1HasCreatedAt && t2HasCreatedAt) {
                        // t2만 createdAt이 있으면 t2가 우선
                        return 1;
                    } else {
                        // 둘 다 createdAt이 없으면 기존 priority 기준 내림차순
                        // 현재는 ID 기준으로 정렬 (나중에 priority 필드 추가 시 수정 필요)
                        return Long.compare(t2.getId(), t1.getId());
                    }
                })
                .collect(Collectors.toList());
    }



    @Transactional
    public GoalResponseDto updateGoalPriority(Long goalId, Long userId, GoalPriorityUpdateRequestDto requestDto) {
        Goal goal = goalService.getGoalById(goalId);
        StudyUser currentUser = studyUserService.getOrThrowIfNotJoined(goal.getStudy().getId(), userId);
        
        // 우선순위 변경은 스터디장만 가능
        if (currentUser.getUserRole() != UserRole.LEADER) {
            throw new RuntimeException("우선순위 변경은 스터디장만 가능합니다.");
        }
        
        // 새로운 정책에 맞게 우선순위 재정렬
        List<Todo> allTodos = todoService.getTodosByGoalId(goalId);
        String newPriorityOrder = calculateNewPriorityOrder(allTodos);
        
        goalService.updatePriorityOrder(goalId, newPriorityOrder);
        
        return GoalResponseDto.builder()
                .id(goal.getId())
                .title(goal.getTitle())
                .priorityOrder(newPriorityOrder)
                .build();
    }

    private void validatePriorityOrder(String priorityOrder, Long goalId) {
        if (priorityOrder == null || priorityOrder.trim().isEmpty()) {
            throw new RuntimeException("우선순위 순서는 필수입니다.");
        }
        
        // 쉼표로 구분된 투두 ID들을 파싱
        String[] todoIds = priorityOrder.split(",");
        
        // 각 투두 ID가 실제로 존재하는지 확인
        for (String todoIdStr : todoIds) {
            try {
                Long todoId = Long.parseLong(todoIdStr.trim());
                Todo todo = todoService.getTodoById(todoId);
                
                // 해당 투두가 이 목표에 속하는지 확인
                if (!todo.getGoal().getId().equals(goalId)) {
                    throw new RuntimeException("투두 ID " + todoId + "는 이 목표에 속하지 않습니다.");
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException("잘못된 투두 ID 형식입니다: " + todoIdStr);
            }
        }
    }

    /**
     * 새로운 우선순위 정책에 따른 우선순위 문자열 생성
     * 1. createdAt이 있는 투두들을 createdAt 기준 오름차순 정렬
     * 2. createdAt이 없는 투두들을 기존 priority 기준 내림차순 정렬
     */
    private String calculateNewPriorityOrder(List<Todo> todos) {
        // 1. createdAt이 있는 투두들을 createdAt 기준 오름차순 정렬
        List<Todo> todosWithCreatedAt = todos.stream()
                .filter(todo -> todo.getCreatedAt() != null)
                .sorted(Comparator.comparing(Todo::getCreatedAt))
                .collect(Collectors.toList());
        
        // 2. createdAt이 없는 투두들을 기존 priority 기준 내림차순 정렬
        List<Todo> todosWithoutCreatedAt = todos.stream()
                .filter(todo -> todo.getCreatedAt() == null)
                .sorted((t1, t2) -> {
                    // 기존 priority 기준 내림차순 정렬
                    // 현재는 ID 기준으로 정렬 (나중에 priority 필드 추가 시 수정 필요)
                    return Long.compare(t2.getId(), t1.getId());
                })
                .collect(Collectors.toList());
        
        // 3. 두 리스트를 합쳐서 우선순위 문자열 생성
        List<Todo> sortedTodos = new ArrayList<>();
        sortedTodos.addAll(todosWithCreatedAt);
        sortedTodos.addAll(todosWithoutCreatedAt);
        
        return sortedTodos.stream()
                .map(todo -> todo.getId().toString())
                .collect(Collectors.joining(","));
    }

    @Transactional
    public GoalResponseDto createGoal(GoalCreateRequestDto requestDto) {
        Goal goal = goalService.createGoal(requestDto.getStudyId(), requestDto.getTitle());
        return GoalResponseDto.builder()
                .id(goal.getId())
                .title(goal.getTitle())
                .priorityOrder(goal.getPriorityOrder())
                .build();
    }

    @Transactional
    public GoalResponseDto updateGoal(Long goalId, GoalUpdateRequestDto requestDto) {
        goalService.updateGoalTitle(goalId, requestDto.getTitle());
        Goal goal = goalService.getGoalById(goalId);
        return GoalResponseDto.builder()
                .id(goal.getId())
                .title(goal.getTitle())
                .priorityOrder(goal.getPriorityOrder())
                .build();
    }

    @Transactional
    public void deleteGoal(Long goalId) {
        goalService.deleteGoal(goalId);
    }
} 