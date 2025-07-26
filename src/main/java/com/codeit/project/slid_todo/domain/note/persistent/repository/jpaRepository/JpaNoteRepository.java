package com.codeit.project.slid_todo.domain.note.persistent.repository.jpaRepository;

import com.codeit.project.slid_todo.domain.note.persistent.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaNoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT n FROM Note n WHERE n.todo.id = :todoId AND n.isDeleted = false")
    Optional<Note> findByTodoIdAndNotDeleted(@Param("todoId") Long todoId);

    @Query("SELECT n FROM Note n WHERE n.id = :noteId AND n.isDeleted = false")
    Optional<Note> findByIdAndNotDeleted(@Param("noteId") Long noteId);

    @Query("SELECT COUNT(n) FROM Note n WHERE n.todo.id = :todoId AND n.isDeleted = false")
    long countByTodoIdAndNotDeleted(@Param("todoId") Long todoId);

    @Query("SELECT n FROM Note n " +
           "LEFT JOIN FETCH n.todo t " +
           "LEFT JOIN FETCH t.goal g " +
           "WHERE g.id = :goalId AND n.isDeleted = false " +
           "AND (:noteContent IS NULL OR n.content LIKE %:noteContent%)")
    List<Note> findByGoalIdAndContentContaining(@Param("goalId") Long goalId, @Param("noteContent") String noteContent);
} 