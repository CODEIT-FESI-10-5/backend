package com.codeit.project.slid_todo.domain.goal.business.service;

import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.domain.goal.errorCode.GoalErrorCode;
import com.codeit.project.slid_todo.domain.goal.persistent.entity.Goal;
import com.codeit.project.slid_todo.domain.goal.persistent.repository.DomainGoalRepository;
import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
import com.codeit.project.slid_todo.domain.study.persistent.repository.DomainStudyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class GoalService {

    private final DomainGoalRepository goalRepository;
    private final DomainStudyRepository studyRepository;

    private static final int MAX_GOALS_PER_STUDY = 10; // 목표 최대 개수 제한

    public Goal createGoal(Long studyId, String title) {
        Study study = studyRepository.getByIdOrThrow(studyId);

        // 목표 개수 제한 확인
        long currentGoalCount = goalRepository.countByStudyId(studyId);
        if (currentGoalCount >= MAX_GOALS_PER_STUDY) {
            throw new BaseException(GoalErrorCode.GOAL_LIMIT_EXCEEDED);
        }

        Goal goal = Goal.builder()
                .study(study)
                .title(title)
                .build();

        goalRepository.save(goal);
        return goal;
    }

    public Goal getGoalById(Long goalId) {
        return goalRepository.getByIdOrThrow(goalId);
    }

    public Goal findGoalWithStudyByGoalId(Long goalId) {
        return goalRepository.findWithStudyByIdOrThrow(goalId);
    }

    public List<Goal> getGoalsByStudyId(Long studyId) {
        return goalRepository.findByStudyId(studyId);
    }

    public void updateGoalTitle(Long goalId, String title) {
        Goal goal = goalRepository.getByIdOrThrow(goalId);
        goal.updateTitle(title);
        goalRepository.save(goal);
    }

    public void deleteGoal(Long goalId) {
        Goal goal = goalRepository.getByIdOrThrow(goalId);
        goal.delete();
        goalRepository.save(goal);
    }
} 