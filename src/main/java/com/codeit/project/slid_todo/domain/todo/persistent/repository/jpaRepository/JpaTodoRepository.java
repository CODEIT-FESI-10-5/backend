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
            "WHERE t.goal.id = :goalId AND t.assignedUser.user.id = :userId AND t.isDeleted = false")
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
} 