package com.codeit.project.slid_todo.domain.note.persistent.repository;

import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.domain.note.errorCode.NoteErrorCode;
import com.codeit.project.slid_todo.domain.note.persistent.entity.Note;
import com.codeit.project.slid_todo.domain.note.persistent.repository.jpaRepository.JpaNoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DomainNoteRepository {

    private final JpaNoteRepository jpaNoteRepository;

    public Note getByIdOrThrow(Long noteId) {
        return jpaNoteRepository.findByIdAndNotDeleted(noteId)
                .orElseThrow(() -> new BaseException(NoteErrorCode.NOT_EXIST_NOTE));
    }

    public Note findByTodoIdOrThrow(Long todoId) {
        return jpaNoteRepository.findByTodoIdAndNotDeleted(todoId)
                .orElseThrow(() -> new BaseException(NoteErrorCode.NOT_EXIST_NOTE));
    }

    public boolean existsByTodoId(Long todoId) {
        return jpaNoteRepository.countByTodoIdAndNotDeleted(todoId) > 0;
    }

    public void save(Note note) {
        jpaNoteRepository.save(note);
    }

    public List<Note> findByGoalIdAndContentContaining(Long goalId, String noteContent) {
        return jpaNoteRepository.findByGoalIdAndContentContaining(goalId, noteContent);
    }
} 