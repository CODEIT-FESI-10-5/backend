package com.codeit.project.slid_todo.domain.goal.persistent.repository.jpaRepository;

import com.codeit.project.slid_todo.domain.goal.persistent.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaGoalRepository extends JpaRepository<Goal, Long> {

    @Query("SELECT g FROM Goal g WHERE g.study.id = :studyId AND g.isDeleted = false")
    List<Goal> findByStudyIdAndNotDeleted(@Param("studyId") Long studyId);

    @Query("SELECT g FROM Goal g WHERE g.id = :goalId AND g.isDeleted = false")
    Optional<Goal> findByIdAndNotDeleted(@Param("goalId") Long goalId);

    @Query("SELECT COUNT(g) FROM Goal g WHERE g.study.id = :studyId AND g.isDeleted = false")
    long countByStudyIdAndNotDeleted(@Param("studyId") Long studyId);
} 