package com.codeit.project.slid_todo.domain.todo.persistent.repository;

import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.domain.todo.errorCode.TodoErrorCode;
import com.codeit.project.slid_todo.domain.todo.persistent.entity.Todo;
import com.codeit.project.slid_todo.domain.todo.persistent.repository.jpaRepository.JpaTodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DomainTodoRepository {

    private final JpaTodoRepository jpaTodoRepository;

    public Todo getByIdOrThrow(Long todoId) {
        return jpaTodoRepository.findByIdAndNotDeleted(todoId)
                .orElseThrow(() -> new BaseException(TodoErrorCode.NOT_EXIST_TODO));
    }

    public List<Todo> findByGoalIdAndUserId(Long goalId, Long userId) {
        return jpaTodoRepository.findByGoalIdAndUserIdAndNotDeleted(goalId, userId);
    }

    public long countCompletedByGoalIdAndUserId(Long goalId, Long userId) {
        return jpaTodoRepository.countCompletedByGoalIdAndUserId(goalId, userId);
    }

    public long countByGoalIdAndUserId(Long goalId, Long userId) {
        return jpaTodoRepository.countByGoalIdAndUserId(goalId, userId);
    }

    public long countCompletedByStudyId(Long studyId) {
        return jpaTodoRepository.countCompletedByStudyId(studyId);
    }

    public long countByStudy(Long studyId) {
        return jpaTodoRepository.countByStudy(studyId);
    }

    public long countByGoalId(Long goalId) {
        return jpaTodoRepository.countByGoalIdAndNotDeleted(goalId);
    }

    public void save(Todo todo) {
        jpaTodoRepository.save(todo);
    }

    public List<Todo> findRecentCompletedTodosByGoalIdAndUserId(Long goalId, Long userId) {
        return jpaTodoRepository.findRecentCompletedTodosByGoalIdAndUserId(goalId, userId);
    }

    public List<Todo> findInProgressTodosByGoalIdAndUserId(Long goalId, Long userId) {
        return jpaTodoRepository.findInProgressTodosByGoalIdAndUserId(goalId, userId);
    }

    public List<Todo> findByGoalId(Long goalId) {
        return jpaTodoRepository.findByGoalId(goalId);
    }
} 