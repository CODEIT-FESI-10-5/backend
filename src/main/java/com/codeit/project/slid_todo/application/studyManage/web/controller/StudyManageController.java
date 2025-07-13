package com.codeit.project.slid_todo.application.studyManage.web.controller;

import com.codeit.project.slid_todo.application.studyManage.business.service.StudyManageFacade;
import com.codeit.project.slid_todo.common.annotation.CheckStudyLeader;
import com.codeit.project.slid_todo.common.annotation.CurrentUser;
import com.codeit.project.slid_todo.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudyManageController {

    private final StudyManageFacade studyManageFacade;

    @PostMapping("/api/study")
    public ResponseEntity<ResponseDto<Void>> createStudy( @CurrentUser Long userId ) {

        studyManageFacade.createStudy(userId);

        ResponseDto<Void> response = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.CREATED.value())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @CheckStudyLeader
    @PostMapping("/api/study/{studyId}")
    public ResponseEntity<ResponseDto<Void>> createStudyGoal( @PathVariable Long studyId, @CurrentUser Long userId ) {

        ResponseDto<Void> response = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.CREATED.value())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
