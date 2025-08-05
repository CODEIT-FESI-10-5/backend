package com.codeit.project.slid_todo.application.UserManage.web.dto;

import com.codeit.project.slid_todo.application.UserManage.Enums.ProfileImageUpdateAction;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

public record EditProfileImgDto() {

    public record Request(
            @Schema(
                    description = """
                            프로필 이미지 변경 동작 옵션
                            - UPLOAD: 새 이미지 업로드
                            - RESET: 기본 이미지로 변경
                            - NONE: 이미지 변경 없음
                            """,
                    example = "UPLOAD"
            )
            ProfileImageUpdateAction profileImageAction,

            @Schema(description = "새 프로필 이미지 파일 (UPLOAD일 경우 필수)", type = "string", format = "binary")
            MultipartFile newImageFile
    ) {
    }

    @Builder
    public record Response(
            String profileImg
    ) {
        public static EditProfileImgDto.Response from(User user) {
            return EditProfileImgDto.Response.builder()
                    .profileImg(
                            user.getImg() != null ?
                                    user.getImg().getStoreImgDir()
                                    : ""
                    )
                    .build();
        }
    }

}
