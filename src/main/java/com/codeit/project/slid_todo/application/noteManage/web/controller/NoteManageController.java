package com.codeit.project.slid_todo.application.noteManage.web.controller;

import com.codeit.project.slid_todo.application.noteManage.business.service.NoteManageFacade;
import com.codeit.project.slid_todo.application.noteManage.web.dto.NoteListRequestDto;
import com.codeit.project.slid_todo.application.noteManage.web.dto.NoteListResponseDto;
import com.codeit.project.slid_todo.application.noteManage.web.dto.NoteDetailResponseDto;
import com.codeit.project.slid_todo.application.noteManage.web.dto.UpdateNoteRequestDto;
import com.codeit.project.slid_todo.common.annotation.CurrentUser;
import com.codeit.project.slid_todo.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Note Management", description = "노트 관리 API")
@SecurityRequirement(name = "bearerAuth")
public class NoteManageController {
    private final NoteManageFacade noteManageFacade;

    @GetMapping("/api/notes")
    @Operation(summary = "노트 목록 조회", description = "목표 하위의 모든 노트 목록을 조회합니다. 제목으로 필터링 가능합니다.")
    public ResponseEntity<ResponseDto<NoteListResponseDto>> getNotesByGoal(
            @ModelAttribute NoteListRequestDto requestDto,
            @CurrentUser Long userId) {

        NoteListResponseDto response = noteManageFacade.getNotesByGoal(requestDto, userId);

        ResponseDto<NoteListResponseDto> responseDto = ResponseDto.<NoteListResponseDto>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .data(response)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/api/notes/{noteId}")
    @Operation(summary = "노트 상세 조회", description = "특정 노트의 상세 정보를 조회합니다.")
    public ResponseEntity<ResponseDto<NoteDetailResponseDto>> getNoteById(
            @Parameter(description = "노트 ID", example = "1") @PathVariable Long noteId) {

        NoteDetailResponseDto response = noteManageFacade.getNoteById(noteId);

        ResponseDto<NoteDetailResponseDto> responseDto = ResponseDto.<NoteDetailResponseDto>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .data(response)
                .build();

        return ResponseEntity.ok(responseDto);
    }



    @PatchMapping("/api/notes/{noteId}")
    @Operation(summary = "노트 수정", description = "기존 노트의 제목과 내용을 수정합니다.")
    public ResponseEntity<ResponseDto<NoteDetailResponseDto>> updateNote(
            @Parameter(description = "노트 ID", example = "1") @PathVariable Long noteId,
            @Valid @RequestBody UpdateNoteRequestDto requestDto) {

        NoteDetailResponseDto response = noteManageFacade.updateNote(noteId, requestDto);

        ResponseDto<NoteDetailResponseDto> responseDto = ResponseDto.<NoteDetailResponseDto>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .data(response)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/api/notes/{noteId}")
    @Operation(summary = "노트 삭제", description = "노트를 소프트 삭제합니다.")
    public ResponseEntity<ResponseDto<Void>> deleteNote(
            @Parameter(description = "노트 ID", example = "1") @PathVariable Long noteId) {

        noteManageFacade.deleteNote(noteId);

        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(responseDto);
    }
} 