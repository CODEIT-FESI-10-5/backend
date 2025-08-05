package com.codeit.project.slid_todo.application.UserManage.web.dto;

import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import lombok.Builder;

public record EditNicknameDto() {

    public record Request(
            String nickname
    ) {
    }

    @Builder
    public record Response(
            String nickname
    ) {
        public static EditNicknameDto.Response from(User user) {
            return EditNicknameDto.Response.builder()
                    .nickname(user.getNickname())
                    .build();
        }
    }

}
