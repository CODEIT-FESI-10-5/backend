package com.codeit.project.slid_todo.common.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginDto(
        @Email
        @NotNull
        String email,

        @NotNull
        String password
) {
}
