package com.codeit.project.slid_todo.application.UserManage.business.service;

import com.codeit.project.slid_todo.application.UserManage.Enums.ProfileImageUpdateAction;
import com.codeit.project.slid_todo.application.UserManage.web.dto.EditProfileDto;
import com.codeit.project.slid_todo.common.util.ImgStore;
import com.codeit.project.slid_todo.common.vo.UploadImg;
import com.codeit.project.slid_todo.domain.user.business.service.UserService;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserManageFacade {

    private final UserService userService;
    private final ImgStore imgStore;

    @Transactional
    public void editProfile(EditProfileDto.Request dto, Long userId) throws IOException {
        User user = userService.findUserById(userId);

        UploadImg newImage = handleProfileImageUpdate(dto, user);

        user.updateProfile(dto.nickname(), newImage);

        if (dto.currentPassword() != null && dto.newPassword() != null) {
            userService.changePassword(user, dto.currentPassword(), dto.newPassword());
        }
    }

    private UploadImg handleProfileImageUpdate(EditProfileDto.Request dto, User user) throws IOException {
        ProfileImageUpdateAction action = dto.profileImageAction();

        if ((action == ProfileImageUpdateAction.RESET || action == ProfileImageUpdateAction.UPLOAD)
                && user.hasCustomImage()) {
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
