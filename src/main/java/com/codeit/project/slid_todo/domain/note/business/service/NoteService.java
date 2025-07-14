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

    public Note createNote(Long todoId, String title, String content) {
        Todo todo = todoRepository.getByIdOrThrow(todoId);
        
        // 이미 노트가 존재하는지 확인
        if (noteRepository.existsByTodoId(todoId)) {
            throw new BaseException(NoteErrorCode.NOTE_ALREADY_EXISTS);
        }

        Note note = Note.builder()
                .todo(todo)
                .title(title)
                .content(content)
                .build();

        noteRepository.save(note);
        return note;
    }

    public Note getNoteById(Long noteId) {
        return noteRepository.getByIdOrThrow(noteId);
    }

    public Note getNoteByTodoId(Long todoId) {
        return noteRepository.findByTodoIdOrThrow(todoId);
    }

    public void updateNote(Long noteId, String title, String content) {
        Note note = noteRepository.getByIdOrThrow(noteId);
        note.updateTitle(title);
        note.updateContent(content);
        noteRepository.save(note);
    }

    public void deleteNote(Long noteId) {
        Note note = noteRepository.getByIdOrThrow(noteId);
        note.delete();
        noteRepository.save(note);
    }
} 