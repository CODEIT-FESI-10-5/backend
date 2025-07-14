package com.codeit.project.slid_todo.domain.goal.persistent.repository;

import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.domain.goal.errorCode.GoalErrorCode;
import com.codeit.project.slid_todo.domain.goal.persistent.entity.Goal;
import com.codeit.project.slid_todo.domain.goal.persistent.repository.jpaRepository.JpaGoalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DomainGoalRepository {

    private final JpaGoalRepository jpaGoalRepository;

    public Goal getByIdOrThrow(Long goalId) {
        return jpaGoalRepository.findByIdAndNotDeleted(goalId)
                .orElseThrow(() -> new BaseException(GoalErrorCode.NOT_EXIST_GOAL));
    }

    public List<Goal> findByStudyId(Long studyId) {
        return jpaGoalRepository.findByStudyIdAndNotDeleted(studyId);
    }

    public long countByStudyId(Long studyId) {
        return jpaGoalRepository.countByStudyIdAndNotDeleted(studyId);
    }

    public void save(Goal goal) {
        jpaGoalRepository.save(goal);
    }
} 