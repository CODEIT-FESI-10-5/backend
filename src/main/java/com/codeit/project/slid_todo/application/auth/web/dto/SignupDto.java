package com.codeit.project.slid_todo.application.auth.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignupDto () {

    @Schema(description = "회원가입 요청")
    public record Request(
            @NotNull(message = "닉네임은 필수값입니다")
            @Schema(description = "닉네임", example = "nickname")
            String nickname,

            @Email
            @NotNull(message = "이메일은 필수값입니다")
            @Schema(description = "이메일", example = "kim@example.com")
            String email,

            @NotNull(message = "비밀번호는 필수값입니다")
            @Schema(description = "비밀번호", example = "password123")
            String password
    ) { }

    public record Response() { }

}
