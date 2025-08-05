package com.codeit.project.slid_todo.application.UserManage.business.service;

import com.codeit.project.slid_todo.application.UserManage.Enums.ProfileImageUpdateAction;
import com.codeit.project.slid_todo.application.UserManage.web.dto.EditNicknameDto;
import com.codeit.project.slid_todo.application.UserManage.web.dto.EditPasswordDto;
import com.codeit.project.slid_todo.application.UserManage.web.dto.EditProfileImgDto;
import com.codeit.project.slid_todo.common.util.ImgStore;
import com.codeit.project.slid_todo.common.vo.UploadImg;
import com.codeit.project.slid_todo.domain.user.business.service.UserService;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserManageFacade {

    private final UserService userService;
    private final ImgStore imgStore;

    @Transactional
    public EditNicknameDto.Response editNickname(EditNicknameDto.Request dto, Long userId) throws IOException {
        User user = userService.findUserById(userId);
        user = userService.updateNickname(user, dto.nickname());
        return EditNicknameDto.Response.from(user);
    }

    @Transactional
    public void editPassword(EditPasswordDto.Request dto, Long userId) throws IOException {
        User user = userService.findUserById(userId);

        if (dto.currentPassword() != null && dto.newPassword() != null) {
            userService.changePassword(user, dto.currentPassword(), dto.newPassword());
        }
    }

    @Transactional
    public EditProfileImgDto.Response editProfileImg(EditProfileImgDto.Request dto, Long userId) throws IOException {
        User user = userService.findUserById(userId);
        UploadImg newImage = handleProfileImageUpdate(dto, user);
        user = userService.updateProfileImg(user, newImage);
        return EditProfileImgDto.Response.from(user);
    }

    private UploadImg handleProfileImageUpdate(EditProfileImgDto.Request dto, User user) throws IOException {
        ProfileImageUpdateAction action = dto.profileImageAction();

        if (shouldDeleteImg(action, user)) {
            imgStore.deleteImage(user.getImg().getStoreImgDir());
        }

        return switch (action) {
            case UPLOAD -> imgStore.storeImg(dto.newImageFile());
            case RESET -> null;
            case NONE -> user.getImg();
        };
    }

    private boolean shouldDeleteImg(ProfileImageUpdateAction action, User user) {
        return (action == ProfileImageUpdateAction.RESET || action == ProfileImageUpdateAction.UPLOAD)
                && user.hasCustomImage();
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userService.findUserById(userId);
        user.deleteUser();
    }
}
