package com.codeit.project.slid_todo.application.UserManage.web.dto;

public record EditPasswordDto() {

    public record Request(
            String currentPassword,
            String newPassword
    ) {
    }

    public record Response() {
    }

}
