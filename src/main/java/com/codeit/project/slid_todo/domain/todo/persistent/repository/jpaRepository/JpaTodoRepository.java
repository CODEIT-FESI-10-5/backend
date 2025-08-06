package com.codeit.project.slid_todo.domain.todo.persistent.repository.jpaRepository;

import com.codeit.project.slid_todo.domain.todo.persistent.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaTodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.note " +
            "LEFT JOIN FETCH t.assignedUser su " +
            "LEFT JOIN FETCH su.user " +
            "WHERE t.id = :todoId AND t.isDeleted = false")
    Optional<Todo> findByIdAndNotDeleted(@Param("todoId") Long todoId);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.note " +
            "LEFT JOIN FETCH t.assignedUser su " +
            "LEFT JOIN FETCH su.user " +
            "WHERE t.goal.id = :goalId AND t.assignedUser.user.id = :userId AND t.isDeleted = false " +
            "ORDER BY t.completedAt ASC NULLS LAST, t.priorityOrder ASC")
    List<Todo> findByGoalIdAndUserIdAndNotDeleted(@Param("goalId") Long goalId, @Param("userId") Long userId);

    @Query("SELECT COUNT(t) FROM Todo t WHERE t.goal.id = :goalId AND t.assignedUser.user.id = :userId AND t.completed = true AND t.isDeleted = false")
    long countCompletedByGoalIdAndUserId(@Param("goalId") Long goalId, @Param("userId") Long userId);

    @Query("SELECT COUNT(t) FROM Todo t WHERE t.goal.id = :goalId AND t.assignedUser.user.id = :userId AND t.isDeleted = false")
    long countByGoalIdAndUserId(@Param("goalId") Long goalId, @Param("userId") Long userId);

    @Query("SELECT COUNT(t) FROM Todo t" +
            " WHERE t.assignedUser.study.id = :studyId" +
            " AND t.completed = true" +
            " AND t.isDeleted = false")
    long countCompletedByStudyId(@Param("studyId") Long studyId);

    @Query("SELECT COUNT(t) FROM Todo t" +
            " WHERE t.assignedUser.study.id = :studyId" +
            " AND t.isDeleted = false")
    long countByStudy(@Param("studyId") Long studyId);


    @Query("SELECT COUNT(t) FROM Todo t WHERE t.goal.id = :goalId AND t.isDeleted = false")
    long countByGoalIdAndNotDeleted(@Param("goalId") Long goalId);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.note " +
            "WHERE t.goal.id = :goalId AND t.assignedUser.user.id = :userId " +
            "AND t.completed = true AND t.isDeleted = false " +
            "ORDER BY t.completedAt DESC")
    List<Todo> findRecentCompletedTodosByGoalIdAndUserId(@Param("goalId") Long goalId, @Param("userId") Long userId);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.note " +
            "WHERE t.goal.id = :goalId AND t.assignedUser.user.id = :userId " +
            "AND t.completed = false AND t.isDeleted = false " +
            "ORDER BY t.shared DESC, t.id ASC")
    List<Todo> findInProgressTodosByGoalIdAndUserId(@Param("goalId") Long goalId, @Param("userId") Long userId);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.note " +
            "WHERE t.goal.id = :goalId AND t.assignedUser.user.id = :userId " +
            "AND t.completed = false AND t.isDeleted = false " +
            "ORDER BY t.priorityOrder ASC")
    List<Todo> findInProgressTodosByGoalIdAndUserIdOrderByPriority(@Param("goalId") Long goalId, @Param("userId") Long userId);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.note " +
            "LEFT JOIN FETCH t.assignedUser su " +
            "LEFT JOIN FETCH su.user " +
            "WHERE t.goal.id = :goalId AND t.isDeleted = false")
    List<Todo> findByGoalId(@Param("goalId") Long goalId);

    @Query("SELECT MAX(t.priorityOrder) FROM Todo t WHERE t.goal.id = :goalId AND t.isDeleted = false")
    Integer findMaxPriorityOrderByGoalId(@Param("goalId") Long goalId);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.note " +
            "LEFT JOIN FETCH t.assignedUser su " +
            "LEFT JOIN FETCH su.user " +
            "WHERE t.goal.id = :goalId AND t.priorityOrder >= :priorityOrder AND t.isDeleted = false " +
            "ORDER BY t.priorityOrder ASC")
    List<Todo> findByGoalIdAndPriorityOrderGreaterThanEqual(@Param("goalId") Long goalId, @Param("priorityOrder") Integer priorityOrder);

    @Query("SELECT COUNT(t) FROM Todo t WHERE t.goal.id = :goalId AND t.isDeleted = false")
    long countByGoalId(@Param("goalId") Long goalId);

    @Query("SELECT COUNT(t) FROM Todo t WHERE t.goal.id = :goalId AND t.assignedUser.user.id = :userId AND t.isDeleted = false")
    long countByGoalIdAndUserIdAndNotDeleted(@Param("goalId") Long goalId, @Param("userId") Long userId);

    @Query("SELECT MAX(t.priorityOrder) FROM Todo t WHERE t.goal.id = :goalId AND t.assignedUser.user.id = :userId AND t.isDeleted = false")
    Integer findMaxPriorityOrderByGoalIdAndUserId(@Param("goalId") Long goalId, @Param("userId") Long userId);

    @Query("SELECT MAX(t.priorityOrder) FROM Todo t WHERE t.goal.id = :goalId AND t.assignedUser.user.id = :userId AND t.completed = false AND t.isDeleted = false")
    Integer findMaxPriorityOrderByGoalIdAndUserIdAndNotCompleted(@Param("goalId") Long goalId, @Param("userId") Long userId);

    @Query("SELECT MAX(t.priorityOrder) FROM Todo t WHERE t.goal.id = :goalId AND t.assignedUser.user.id = :userId AND t.completed = false AND t.id != :excludeTodoId AND t.isDeleted = false")
    Integer findMaxPriorityOrderByGoalIdAndUserIdAndNotCompletedExcludingTodo(@Param("goalId") Long goalId, @Param("userId") Long userId, @Param("excludeTodoId") Long excludeTodoId);

    @Query("SELECT MAX(t.priorityOrder) FROM Todo t WHERE t.goal.id = :goalId AND t.id != :excludeTodoId AND t.isDeleted = false")
    Integer findMaxPriorityOrderByGoalIdExcludingTodo(@Param("goalId") Long goalId, @Param("excludeTodoId") Long excludeTodoId);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.note " +
            "LEFT JOIN FETCH t.assignedUser su " +
            "LEFT JOIN FETCH su.user " +
            "WHERE t.goal.id = :goalId AND t.priorityOrder > :priorityOrder AND t.isDeleted = false " +
            "ORDER BY t.priorityOrder ASC")
    List<Todo> findByGoalIdAndPriorityOrderGreaterThan(@Param("goalId") Long goalId, @Param("priorityOrder") Integer priorityOrder);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.note " +
            "LEFT JOIN FETCH t.assignedUser su " +
            "LEFT JOIN FETCH su.user " +
            "WHERE t.goal.id = :goalId AND t.assignedUser.user.id = :userId AND t.priorityOrder >= :priorityOrder AND t.isDeleted = false " +
            "ORDER BY t.priorityOrder ASC")
    List<Todo> findByGoalIdAndUserIdAndPriorityOrderGreaterThanEqual(@Param("goalId") Long goalId, @Param("userId") Long userId, @Param("priorityOrder") Integer priorityOrder);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.note " +
            "LEFT JOIN FETCH t.assignedUser su " +
            "LEFT JOIN FETCH su.user " +
            "WHERE t.goal.id = :goalId AND t.assignedUser.user.id = :userId AND t.priorityOrder <= :priorityOrder AND t.isDeleted = false " +
            "ORDER BY t.priorityOrder ASC")
    List<Todo> findByGoalIdAndUserIdAndPriorityOrderLessThanEqual(@Param("goalId") Long goalId, @Param("userId") Long userId, @Param("priorityOrder") Integer priorityOrder);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.note " +
            "LEFT JOIN FETCH t.assignedUser su " +
            "LEFT JOIN FETCH su.user " +
            "WHERE t.goal.id = :goalId AND t.assignedUser.user.id = :userId AND t.priorityOrder BETWEEN :startPriority AND :endPriority AND t.isDeleted = false " +
            "ORDER BY t.priorityOrder ASC")
    List<Todo> findByGoalIdAndUserIdAndPriorityOrderBetween(@Param("goalId") Long goalId, @Param("userId") Long userId, @Param("startPriority") Integer startPriority, @Param("endPriority") Integer endPriority);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.note " +
            "LEFT JOIN FETCH t.assignedUser su " +
            "LEFT JOIN FETCH su.user " +
            "WHERE t.goal.id = :goalId AND t.assignedUser.user.id = :userId AND t.priorityOrder > :priorityOrder AND t.isDeleted = false " +
            "ORDER BY t.priorityOrder ASC")
    List<Todo> findByGoalIdAndUserIdAndPriorityOrderGreaterThan(@Param("goalId") Long goalId, @Param("userId") Long userId, @Param("priorityOrder") Integer priorityOrder);
} 