package com.codeit.project.slid_todo.domain.note.business.service;

import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.domain.note.errorCode.NoteErrorCode;
import com.codeit.project.slid_todo.domain.note.persistent.entity.Note;
import com.codeit.project.slid_todo.domain.note.persistent.repository.DomainNoteRepository;
import com.codeit.project.slid_todo.domain.todo.persistent.entity.Todo;
import com.codeit.project.slid_todo.domain.todo.persistent.repository.DomainTodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class NoteService {

    private final DomainNoteRepository noteRepository;
    private final DomainTodoRepository todoRepository;



    public Note getNoteById(Long noteId) {
        return noteRepository.getByIdOrThrow(noteId);
    }

    public Note getNoteByTodoId(Long todoId) {
        return noteRepository.findByTodoIdOrThrow(todoId);
    }

    public void updateNote(Long noteId, String content) {
        Note note = noteRepository.getByIdOrThrow(noteId);
        note.updateContent(content);
        noteRepository.save(note);
    }

    public void deleteNote(Long noteId) {
        Note note = noteRepository.getByIdOrThrow(noteId);
        noteRepository.delete(note);
    }
} 