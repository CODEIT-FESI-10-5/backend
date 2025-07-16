package com.codeit.project.slid_todo.application.UserManage.web.controller;

import com.codeit.project.slid_todo.application.UserManage.business.service.UserManageFacade;
import com.codeit.project.slid_todo.application.UserManage.web.dto.EditProfileDto;
import com.codeit.project.slid_todo.common.annotation.CurrentUser;
import com.codeit.project.slid_todo.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "User Management", description = "유저 관리 API")
@RestController
@RequiredArgsConstructor
public class UserManageController {

    private final UserManageFacade userManageFacade;

    @PatchMapping(
            value = "/api/user",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "마이페이지 프로필 수정", description = "닉네임과 프로필 이미지를 수정합니다.")
    public ResponseEntity<ResponseDto<Void>> editProfile(
            @ModelAttribute EditProfileDto.Request editProfileDto,
            @CurrentUser Long userId
    ) throws IOException {
        userManageFacade.editProfile(editProfileDto, userId);

        ResponseDto<Void> response = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
