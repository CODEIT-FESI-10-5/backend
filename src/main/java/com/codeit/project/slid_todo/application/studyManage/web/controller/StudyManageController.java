package com.codeit.project.slid_todo.application.studyManage.web.controller;

import com.codeit.project.slid_todo.application.studyManage.business.service.StudyManageFacade;
import com.codeit.project.slid_todo.application.studyManage.web.dto.*;
import com.codeit.project.slid_todo.common.annotation.CheckStudyLeader;
import com.codeit.project.slid_todo.common.annotation.CurrentUser;
import com.codeit.project.slid_todo.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Study Management", description = "스터디 관리 API")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class StudyManageController {

    private final StudyManageFacade studyManageFacade;

    @PostMapping("/api/study")
    @Operation(summary = "스터디 생성", description = "새로운 스터디를 생성합니다.")
    public ResponseEntity<ResponseDto<Void>> createStudy(
            @Parameter(hidden = true) @CurrentUser Long userId) {

        studyManageFacade.createStudy(userId);

        ResponseDto<Void> response = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.CREATED.value())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @CheckStudyLeader
    @PatchMapping("/api/study/{studyId}/info")
    @Operation(summary = "스터디 정보 수정", description = "스터디 제목과 설명을 수정합니다.")
    public ResponseEntity<ResponseDto<Void>> editStudy(
            @RequestBody EditStudyInfoDto.Request editStudyDto,
            @Parameter(description = "스터디 ID") @PathVariable Long studyId,
            @Parameter(hidden = true) @CurrentUser Long userId) throws IOException {

        studyManageFacade.updateStudyInfo(editStudyDto, studyId);

        ResponseDto<Void> response = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CheckStudyLeader
    @PatchMapping(
            value = "/api/study/{studyId}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "스터디 이미지 수정", description = "스터디 이미지를 수정합니다.")
    public ResponseEntity<ResponseDto<Void>> editStudyImage(
            @ModelAttribute EditStudyImageDto.Request dto,
            @Parameter(description = "스터디 ID") @PathVariable Long studyId,
            @Parameter(hidden = true) @CurrentUser Long userId) throws IOException {

        studyManageFacade.updateStudyImage(dto, studyId);

        ResponseDto<Void> response = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/api/study/join")
    @Operation(summary = "스터디 참가", description = "초대 코드를 이용하여 스터디에 참여합니다.")
    public ResponseEntity<ResponseDto<Void>> joinStudy(
            @Valid @RequestBody JoinStudyDto.Request joinStudyDto,
            @Parameter(hidden = true) @CurrentUser Long userId
    ) {

        studyManageFacade.joinStudy(joinStudyDto, studyId, userId);

        ResponseDto<Void> response = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/api/study")
    @Operation(summary = "스터디 목록 조회", description = "참여 중인 모든 스터디 목록을 조회합니다.")
    public ResponseEntity<ResponseDto<StudyListDto.Response>> getStudyList(
            @Parameter(hidden = true) @CurrentUser Long userId) {
        StudyListDto.Response responseData = studyManageFacade.getStudyList(userId);

        ResponseDto<StudyListDto.Response> response = ResponseDto.<StudyListDto.Response>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .data(responseData)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/api/study/{studyId}")
    @Operation(summary = "스터디 상세 조회", description = "스터디 상세 정보 및 참여 멤버, 진행률 등을 조회합니다.")
    public ResponseEntity<ResponseDto<StudyDetailsDto.Response>> getStudyDetail(
            @Parameter(description = "스터디 ID") @PathVariable Long studyId,
            @Parameter(hidden = true) @CurrentUser Long userId
    ) {

        StudyDetailsDto.Response responseData = studyManageFacade.getStudyDetail(studyId, userId);

        ResponseDto<StudyDetailsDto.Response> response = ResponseDto.<StudyDetailsDto.Response>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .data(responseData)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CheckStudyLeader
    @DeleteMapping("/api/study/{studyId}")
    @Operation(summary = "스터디 삭제", description = "스터디 삭제 기능입니다.")
    public ResponseEntity<ResponseDto<Void>> deleteStudy(
            @Parameter(description = "스터디 ID") @PathVariable Long studyId,
            @Parameter(hidden = true) @CurrentUser Long userId
    ) {

        studyManageFacade.deleteStudy(studyId, userId);

        ResponseDto<Void> response = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
