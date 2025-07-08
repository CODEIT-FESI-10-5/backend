package com.codeit.project.slid_todo.application.auth.web.dto;

import jakarta.validation.constraints.NotNull;

public record SignupDto () {

    public record Request(
            @NotNull(message = "email은 필수값입니다")
            String name,

            @NotNull(message = "email은 필수값입니다")
            String email,

            @NotNull(message = "password은 필수값입니다")
            String password
    ) { }

    public record Response() { }

}
