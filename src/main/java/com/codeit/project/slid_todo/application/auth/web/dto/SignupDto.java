package com.codeit.project.slid_todo.application.auth.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignupDto () {

    public record Request(
            @NotNull(message = "이름은 필수값입니다")
            String name,

            @Email
            @NotNull(message = "이메일은 필수값입니다")
            String email,

            @NotNull(message = "비밀번호는 필수값입니다")
            String password
    ) { }

    public record Response() { }

}
