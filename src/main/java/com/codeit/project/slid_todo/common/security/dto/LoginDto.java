package com.codeit.project.slid_todo.common.security.dto;

import com.codeit.project.slid_todo.common.security.vo.CustomUserDetails;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record LoginDto() {

    public record Request(
            @Email
            @NotNull
            String email,

            @NotNull
            String password
    ) {
    }

    @Builder
    public record Response(
            String email,
            String nickname,
            String profileImg
    ) {

        public static LoginDto.Response from(CustomUserDetails customUserDetails) {
            return Response.builder()
                    .email(customUserDetails.getUsername())
                    .nickname(customUserDetails.getNickname())
                    .profileImg(customUserDetails.getImgDir())
                    .build();
        }
    }

}
