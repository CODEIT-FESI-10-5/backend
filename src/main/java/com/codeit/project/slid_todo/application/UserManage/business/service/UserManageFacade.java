package com.codeit.project.slid_todo.application.UserManage.business.service;

import com.codeit.project.slid_todo.application.UserManage.Enums.ProfileImageUpdateAction;
import com.codeit.project.slid_todo.application.UserManage.web.dto.EditProfileDto;
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
    public void editProfile(EditProfileDto.Request editProfileDto, Long userId) throws IOException {
        User user = userService.findUserById(userId);
        String newNickname = editProfileDto.nickname();
        UploadImg newImage = null;

        if(shouldDeleteImg(editProfileDto.profileImageAction(), user)) {
            imgStore.deleteImage(user.getImg().getStoreImgDir());
        }

        log.info("editProfileDto.action={}", editProfileDto.profileImageAction());

        newImage = switch (editProfileDto.profileImageAction()) {
            case UPLOAD -> imgStore.storeImg(editProfileDto.newImageFile());
            case RESET -> null;
            case NONE -> user.getImg();
        };

        log.info("newImage={}", newImage);

        user.updateProfile(newNickname, newImage);
    }

    private boolean shouldDeleteImg(ProfileImageUpdateAction action, User user) {
        return (action == ProfileImageUpdateAction.RESET || action == ProfileImageUpdateAction.UPLOAD)
                && user.hasCustomImage();
    }
}
