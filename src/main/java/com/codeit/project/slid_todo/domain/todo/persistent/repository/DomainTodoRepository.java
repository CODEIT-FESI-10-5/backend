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

    public List<Todo> findInProgressTodosByGoalIdAndUserIdOrderByPriority(Long goalId, Long userId) {
        return jpaTodoRepository.findInProgressTodosByGoalIdAndUserIdOrderByPriority(goalId, userId);
    }

    public List<Todo> findByGoalId(Long goalId) {
        return jpaTodoRepository.findByGoalId(goalId);
    }

    public Integer findMaxPriorityOrderByGoalId(Long goalId) {
        return jpaTodoRepository.findMaxPriorityOrderByGoalId(goalId);
    }

    public List<Todo> findByGoalIdAndPriorityOrderGreaterThanEqual(Long goalId, Integer priorityOrder) {
        return jpaTodoRepository.findByGoalIdAndPriorityOrderGreaterThanEqual(goalId, priorityOrder);
    }

    public long countByGoalIdAndNotDeleted(Long goalId) {
        return jpaTodoRepository.countByGoalId(goalId);
    }

    public long countByGoalIdAndUserIdAndNotDeleted(Long goalId, Long userId) {
        return jpaTodoRepository.countByGoalIdAndUserIdAndNotDeleted(goalId, userId);
    }

    public Integer findMaxPriorityOrderByGoalIdAndUserId(Long goalId, Long userId) {
        return jpaTodoRepository.findMaxPriorityOrderByGoalIdAndUserId(goalId, userId);
    }

    public Integer findMaxPriorityOrderByGoalIdAndUserIdAndNotCompleted(Long goalId, Long userId) {
        return jpaTodoRepository.findMaxPriorityOrderByGoalIdAndUserIdAndNotCompleted(goalId, userId);
    }

    public Integer findMaxPriorityOrderByGoalIdAndUserIdAndNotCompletedExcludingTodo(Long goalId, Long userId, Long excludeTodoId) {
        return jpaTodoRepository.findMaxPriorityOrderByGoalIdAndUserIdAndNotCompletedExcludingTodo(goalId, userId, excludeTodoId);
    }

    public List<Todo> findByGoalIdAndUserIdAndPriorityOrderGreaterThanEqual(Long goalId, Long userId, Integer priorityOrder) {
        return jpaTodoRepository.findByGoalIdAndUserIdAndPriorityOrderGreaterThanEqual(goalId, userId, priorityOrder);
    }

    public List<Todo> findByGoalIdAndUserIdAndPriorityOrderLessThanEqual(Long goalId, Long userId, Integer priorityOrder) {
        return jpaTodoRepository.findByGoalIdAndUserIdAndPriorityOrderLessThanEqual(goalId, userId, priorityOrder);
    }

    public List<Todo> findByGoalIdAndUserIdAndPriorityOrderBetween(Long goalId, Long userId, Integer startPriority, Integer endPriority) {
        return jpaTodoRepository.findByGoalIdAndUserIdAndPriorityOrderBetween(goalId, userId, startPriority, endPriority);
    }

    public List<Todo> findByGoalIdAndUserIdAndPriorityOrderGreaterThan(Long goalId, Long userId, Integer priorityOrder) {
        return jpaTodoRepository.findByGoalIdAndUserIdAndPriorityOrderGreaterThan(goalId, userId, priorityOrder);
    }

    public Integer findMaxPriorityOrderByGoalIdExcludingTodo(Long goalId, Long excludeTodoId) {
        return jpaTodoRepository.findMaxPriorityOrderByGoalIdExcludingTodo(goalId, excludeTodoId);
    }

    public List<Todo> findByGoalIdAndPriorityOrderGreaterThan(Long goalId, Integer priorityOrder) {
        return jpaTodoRepository.findByGoalIdAndPriorityOrderGreaterThan(goalId, priorityOrder);
    }

    public void delete(Todo todo) {
        jpaTodoRepository.delete(todo);
    }
} 