package com.codeit.project.slid_todo.application.noteManage.business.service;

import com.codeit.project.slid_todo.application.noteManage.web.dto.NoteListRequestDto;
import com.codeit.project.slid_todo.application.noteManage.web.dto.NoteListResponseDto;
import com.codeit.project.slid_todo.application.noteManage.web.dto.NoteDetailResponseDto;
import com.codeit.project.slid_todo.application.noteManage.web.dto.UpdateNoteRequestDto;
import com.codeit.project.slid_todo.domain.goal.persistent.entity.Goal;
import com.codeit.project.slid_todo.domain.goal.business.service.GoalService;
import com.codeit.project.slid_todo.domain.todo.business.service.TodoService;
import com.codeit.project.slid_todo.domain.todo.persistent.entity.Todo;
import com.codeit.project.slid_todo.domain.note.business.service.NoteService;
import com.codeit.project.slid_todo.domain.note.persistent.entity.Note;
import com.codeit.project.slid_todo.domain.note.persistent.repository.DomainNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoteManageFacade {
    private final GoalService goalService;
    private final TodoService todoService;
    private final NoteService noteService;
    private final DomainNoteRepository noteRepository;

    public NoteListResponseDto getNotesByGoal(NoteListRequestDto requestDto) {
        Long goalId = requestDto.getGoalId();
        String noteContent = requestDto.getNoteContent();
        
        // 목표 정보 조회
        Goal goal = goalService.getGoalById(goalId);
        
        // N+1 문제 개선: 직접 Note를 조회
        List<Note> notes = noteRepository.findByGoalIdAndContentContaining(goalId, noteContent);

        List<NoteListResponseDto.NoteData> noteDataList = notes.stream()
                .map(note -> NoteListResponseDto.NoteData.builder()
                        .id(note.getId())
                        .todoTitle(note.getTodo().getContent())
                        .content(note.getContent())
                        .createdAt(note.getCreatedAt())
                        .updatedAt(note.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        return NoteListResponseDto.builder()
                .studyGoalTitle(goal.getTitle())
                .notes(noteDataList)
                .build();
    }

    public NoteDetailResponseDto getNoteById(Long noteId) {
        Note note = noteService.getNoteById(noteId);
        
        return NoteDetailResponseDto.builder()
                .id(note.getId())
                .studyGoalTitle(note.getTodo().getGoal().getTitle())
                .todoTitle(note.getTodo().getContent())
                .content(note.getContent())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }



    @Transactional
    public NoteDetailResponseDto updateNote(Long noteId, UpdateNoteRequestDto requestDto) {
        noteService.updateNote(noteId, requestDto.getContent());
        Note note = noteService.getNoteById(noteId);
        
        return NoteDetailResponseDto.builder()
                .id(note.getId())
                .studyGoalTitle(note.getTodo().getGoal().getTitle())
                .todoTitle(note.getTodo().getContent())
                .content(note.getContent())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }

    @Transactional
    public void deleteNote(Long noteId) {
        noteService.deleteNote(noteId);
    }
} 