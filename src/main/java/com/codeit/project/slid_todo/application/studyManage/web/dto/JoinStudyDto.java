package com.codeit.project.slid_todo.application.studyManage.web.dto;

import jakarta.validation.constraints.NotNull;

public record JoinStudyDto() {

    public record Request(
            @NotNull
            String inviteCode
    ) {

    }

    public record Response() {

    }
}
