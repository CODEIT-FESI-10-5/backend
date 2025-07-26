package com.codeit.project.slid_todo.application.goalManage.business.service;

import com.codeit.project.slid_todo.application.goalManage.web.dto.GoalCreateRequestDto;
import com.codeit.project.slid_todo.application.goalManage.web.dto.GoalDetailResponseDto;
import com.codeit.project.slid_todo.application.goalManage.web.dto.GoalListResponseDto;
import com.codeit.project.slid_todo.application.goalManage.web.dto.GoalResponseDto;
import com.codeit.project.slid_todo.application.goalManage.web.dto.GoalUpdateRequestDto;
import com.codeit.project.slid_todo.domain.goal.business.service.GoalService;
import com.codeit.project.slid_todo.domain.goal.persistent.entity.Goal;
import com.codeit.project.slid_todo.domain.note.business.service.NoteService;
import com.codeit.project.slid_todo.domain.studyUser.business.service.StudyUserService;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
import com.codeit.project.slid_todo.domain.todo.business.service.TodoService;
import com.codeit.project.slid_todo.domain.todo.persistent.entity.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        
        // 투두 목록 (우선순위 없이)
        List<Todo> sortedTodos = userTodos;
        
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
                            .order(todo.getPriorityOrder())
                            .shared(todo.isShared())
                            .priorityOrder(todo.getPriorityOrder())
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













    @Transactional
    public GoalResponseDto createGoal(GoalCreateRequestDto requestDto) {
        Goal goal = goalService.createGoal(requestDto.getStudyId(), requestDto.getTitle());
        return GoalResponseDto.builder()
                .id(goal.getId())
                .title(goal.getTitle())
                .build();
    }

    @Transactional
    public GoalResponseDto updateGoal(Long goalId, GoalUpdateRequestDto requestDto) {
        goalService.updateGoalTitle(goalId, requestDto.getTitle());
        Goal goal = goalService.getGoalById(goalId);
        return GoalResponseDto.builder()
                .id(goal.getId())
                .title(goal.getTitle())
                .build();
    }

    @Transactional
    public void deleteGoal(Long goalId) {
        goalService.deleteGoal(goalId);
    }

    public GoalListResponseDto getGoalsByStudyId(Long studyId) {
        List<Goal> goals = goalService.getGoalsByStudyId(studyId);
        
        List<GoalResponseDto> goalResponseDtos = goals.stream()
                .map(goal -> GoalResponseDto.builder()
                        .id(goal.getId())
                        .title(goal.getTitle())
                        .build())
                .collect(Collectors.toList());
        
        return GoalListResponseDto.builder()
                .studyId(studyId)
                .goals(goalResponseDtos)
                .totalCount(goalResponseDtos.size())
                .build();
    }
} 