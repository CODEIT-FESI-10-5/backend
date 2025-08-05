package com.codeit.project.slid_todo.application.UserManage.web.controller;

import com.codeit.project.slid_todo.application.UserManage.business.service.UserManageFacade;
import com.codeit.project.slid_todo.application.UserManage.web.dto.EditNicknameDto;
import com.codeit.project.slid_todo.application.UserManage.web.dto.EditPasswordDto;
import com.codeit.project.slid_todo.application.UserManage.web.dto.EditProfileImgDto;
import com.codeit.project.slid_todo.common.annotation.CurrentUser;
import com.codeit.project.slid_todo.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "User Management", description = "유저 관리 API")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserManageController {

    private final UserManageFacade userManageFacade;

    @PatchMapping("/api/user/nickname")
    @Operation(summary = "마이페이지 닉네임 수정", description = "닉네임을 수정합니다.")
    public ResponseEntity<ResponseDto<EditNicknameDto.Response>> editNickname(
            @RequestBody EditNicknameDto.Request editNicknameDto,
            @CurrentUser Long userId
    ) throws IOException {

        EditNicknameDto.Response responseData = userManageFacade.editNickname(editNicknameDto, userId);

        ResponseDto<EditNicknameDto.Response> response = ResponseDto.<EditNicknameDto.Response>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .data(responseData)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/api/user/password")
    @Operation(summary = "마이페이지 비밀번호 수정", description = "비밀번호를 수정합니다.")
    public ResponseEntity<ResponseDto<Void>> editPassword(
            @RequestBody EditPasswordDto.Request editPasswordDto,
            @CurrentUser Long userId
    ) throws IOException {

        userManageFacade.editPassword(editPasswordDto, userId);

        ResponseDto<Void> response = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping(
            value = "/api/user/profileImg",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "마이페이지 프로필 이미지 수정", description = "프로필 이미지를 수정합니다.")
    public ResponseEntity<ResponseDto<EditProfileImgDto.Response>> editProfileImg(
            @ModelAttribute EditProfileImgDto.Request editProfileImgDto,
            @CurrentUser Long userId
    ) throws IOException {

        EditProfileImgDto.Response responseData = userManageFacade.editProfileImg(editProfileImgDto, userId);

        ResponseDto<EditProfileImgDto.Response> response = ResponseDto.<EditProfileImgDto.Response>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .data(responseData)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/api/user")
    @Operation(summary = "마이페이지 회원 탈퇴", description = "회원 탈퇴 기능입니다.")
    public ResponseEntity<ResponseDto<Void>> deleteProfile(
            @CurrentUser Long userId) {

        userManageFacade.deleteUser(userId);

        ResponseDto<Void> response = ResponseDto.<Void>builder()
                .httpStatusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
