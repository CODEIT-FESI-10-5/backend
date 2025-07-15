package com.codeit.project.slid_todo.application.studyManage.web.controller;

import com.codeit.project.slid_todo.application.studyManage.business.service.StudyManageFacade;
import com.codeit.project.slid_todo.application.studyManage.web.dto.EditStudyDto;
import com.codeit.project.slid_todo.application.studyManage.web.dto.JoinStudyDto;
import com.codeit.project.slid_todo.application.studyManage.web.dto.StudyDetailsDto;
import com.codeit.project.slid_todo.application.studyManage.web.dto.StudyListDto;
import com.codeit.project.slid_todo.common.annotation.CheckStudyLeader;
import com.codeit.project.slid_todo.common.annotation.CurrentUser;
import com.codeit.project.slid_todo.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class StudyManageController {

    private final StudyManageFacade studyManageFacade;

    @PostMapping("/api/study")
    public ResponseEntity<ResponseDto<Void>> createStudy(@CurrentUser Long userId) {

        studyManageFacade.createStudy(userId);

        ResponseDto<Void> response = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.CREATED.value())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @CheckStudyLeader
    @PatchMapping("/api/study/{studyId}")
    public ResponseEntity<ResponseDto<Void>> editStudy(
            @ModelAttribute EditStudyDto.Request editStudyDto,
            @PathVariable Long studyId,
            @CurrentUser Long userId) throws IOException {

        studyManageFacade.updateStudy(editStudyDto, studyId);

        ResponseDto<Void> response = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/api/study/{studyId}/join")
    public ResponseEntity<ResponseDto<Void>> joinStudy(
            @PathVariable Long studyId,
            @RequestBody JoinStudyDto.Request joinStudyDto,
            @CurrentUser Long userId
    ) {

        studyManageFacade.joinStudy(joinStudyDto, studyId, userId);

        ResponseDto<Void> response = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/api/study")
    public ResponseEntity<ResponseDto<StudyListDto.Response>> getStudyList(@CurrentUser Long userId) {
        StudyListDto.Response responseData = studyManageFacade.getStudyList(userId);

        ResponseDto<StudyListDto.Response> response = ResponseDto.<StudyListDto.Response>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .data(responseData)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/api/study/{studyId}")
    public ResponseEntity<ResponseDto<StudyDetailsDto.Response>> getStudyDetail(
            @PathVariable Long studyId,
            @CurrentUser Long userId
    ) {

        StudyDetailsDto.Response responseData = studyManageFacade.getStudyDetail(studyId, userId);

         ResponseDto<StudyDetailsDto.Response> response = ResponseDto.<StudyDetailsDto.Response>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .data(responseData)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @CheckStudyLeader
    @PostMapping("/api/study/{studyId}")
    public ResponseEntity<ResponseDto<Void>> createStudyGoal(@PathVariable Long studyId, @CurrentUser Long userId) {

        ResponseDto<Void> response = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.CREATED.value())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
