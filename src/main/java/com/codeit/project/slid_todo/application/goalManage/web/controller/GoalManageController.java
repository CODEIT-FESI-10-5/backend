package com.codeit.project.slid_todo.application.goalManage.web.controller;

import com.codeit.project.slid_todo.application.goalManage.business.service.GoalManageFacade;
import com.codeit.project.slid_todo.application.goalManage.web.dto.GoalCreateRequestDto;
import com.codeit.project.slid_todo.application.goalManage.web.dto.GoalDetailResponseDto;
import com.codeit.project.slid_todo.application.goalManage.web.dto.GoalListResponseDto;
import com.codeit.project.slid_todo.application.goalManage.web.dto.GoalResponseDto;
import com.codeit.project.slid_todo.application.goalManage.web.dto.GoalUpdateRequestDto;
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
@Tag(name = "Goal Management", description = "목표 관리 API")
@SecurityRequirement(name = "bearerAuth")
public class GoalManageController {

    private final GoalManageFacade goalManageFacade;

    @GetMapping("/api/goals/{goalId}")
    @Operation(summary = "목표 하위 정보 조회", description = "목표와 관련된 투두 목록, 팀 진행도를 조회합니다.")
    public ResponseEntity<ResponseDto<GoalDetailResponseDto>> getGoal(
            @Parameter(description = "목표 ID", example = "1") @PathVariable Long goalId,
            @CurrentUser Long userId) {

        GoalDetailResponseDto response = goalManageFacade.getGoal(goalId, userId);

        ResponseDto<GoalDetailResponseDto> responseDto = ResponseDto.<GoalDetailResponseDto>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .data(response)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/api/goals")
    @Operation(summary = "목표 생성", description = "새로운 목표(Goal)를 생성합니다.")
    public ResponseEntity<ResponseDto<GoalResponseDto>> createGoal(
            @Valid @RequestBody GoalCreateRequestDto requestDto) {
        GoalResponseDto response = goalManageFacade.createGoal(requestDto);
        ResponseDto<GoalResponseDto> responseDto = ResponseDto.<GoalResponseDto>builder()
                .httpStatusCode(HttpStatus.CREATED.value())
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PatchMapping("/api/goals/{goalId}")
    @Operation(summary = "목표 수정", description = "기존 목표의 제목을 수정합니다.")
    public ResponseEntity<ResponseDto<GoalResponseDto>> updateGoal(
            @PathVariable Long goalId,
            @Valid @RequestBody GoalUpdateRequestDto requestDto) {
        GoalResponseDto response = goalManageFacade.updateGoal(goalId, requestDto);
        ResponseDto<GoalResponseDto> responseDto = ResponseDto.<GoalResponseDto>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .data(response)
                .build();
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/api/goals/{goalId}")
    @Operation(summary = "목표 삭제", description = "목표를 소프트 딜리트합니다.")
    public ResponseEntity<ResponseDto<Void>> deleteGoal(@PathVariable Long goalId) {
        goalManageFacade.deleteGoal(goalId);
        ResponseDto<Void> responseDto = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(responseDto);
    }



    @GetMapping("/api/studies/{studyId}/goals")
    @Operation(summary = "스터디별 목표 목록 조회", description = "특정 스터디의 모든 목표 목록을 조회합니다.")
    public ResponseEntity<ResponseDto<GoalListResponseDto>> getGoalsByStudyId(
            @Parameter(description = "스터디 ID", example = "1") @PathVariable Long studyId) {
        
        GoalListResponseDto response = goalManageFacade.getGoalsByStudyId(studyId);
        
        ResponseDto<GoalListResponseDto> responseDto = ResponseDto.<GoalListResponseDto>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .data(response)
                .build();
        
        return ResponseEntity.ok(responseDto);
    }
} 