package com.codeit.project.slid_todo.application.todoManage.web.controller;

import com.codeit.project.slid_todo.application.todoManage.business.service.TodoManageFacade;
import com.codeit.project.slid_todo.application.todoManage.web.dto.*;
import com.codeit.project.slid_todo.common.annotation.CurrentUser;
import com.codeit.project.slid_todo.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Todo Management", description = "투두 리스트 관리 API")
public class TodoManageController {

    private final TodoManageFacade todoManageFacade;

    @GetMapping("/api/todos/{goalId}")
    @Operation(summary = "투두 목록 조회", description = "목표 하위의 투두 목록을 우선순위 순서대로 조회합니다.")
    public ResponseEntity<ResponseDto<TodoListResponseDto>> getTodos(
            @Parameter(description = "목표 ID", example = "1") @PathVariable("goalId") Long goalId,
            @CurrentUser Long userId) {

        TodoListResponseDto response = todoManageFacade.getTodoList(goalId, userId);

        ResponseDto<TodoListResponseDto> responseDto = ResponseDto.<TodoListResponseDto>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .data(response)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/api/todos/{goalId}/todo")
    @Operation(summary = "투두 생성", description = "새로운 투두를 생성합니다. 공통 투두는 스터디장만 생성 가능합니다.")
    public ResponseEntity<ResponseDto<Void>> createTodo(
            @Parameter(description = "목표 ID", example = "1") @PathVariable("goalId") Long goalId,
            @CurrentUser Long userId,
            @Valid @RequestBody CreateTodoRequestDto requestDto) {

        todoManageFacade.createTodo(goalId, userId, requestDto);

        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.CREATED.value())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PatchMapping("/api/todos/{goalId}/todo/{todoIdValue}")
    @Operation(summary = "투두 수정", description = "기존 투두의 내용과 완료 상태를 수정합니다.")
    public ResponseEntity<ResponseDto<Void>> updateTodo(
            @Parameter(description = "목표 ID", example = "1") @PathVariable("goalId") Long goalId,
            @Parameter(description = "투두 ID", example = "1") @PathVariable("todoIdValue") Long todoId,
            @CurrentUser Long userId,
            @Valid @RequestBody UpdateTodoRequestDto requestDto) {

        todoManageFacade.updateTodo(goalId, todoId, userId, requestDto);

        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/api/todos/{goalId}/order")
    @Operation(summary = "투두 순서 변경", description = "투두의 우선순위 순서를 변경합니다. 스터디장만 가능합니다.")
    public ResponseEntity<ResponseDto<Void>> updateOrder(
            @Parameter(description = "목표 ID", example = "1") @PathVariable("goalId") Long goalId,
            @CurrentUser Long userId,
            @Valid @RequestBody UpdateOrderRequestDto requestDto) {

        todoManageFacade.updateOrder(goalId, userId, requestDto);

        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(responseDto);
    }
} 