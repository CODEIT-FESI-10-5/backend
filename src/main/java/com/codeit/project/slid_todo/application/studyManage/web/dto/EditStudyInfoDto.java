package com.codeit.project.slid_todo.application.studyManage.web.dto;

public record EditStudyInfoDto() {

    public record Request(
            String title,

            String description
    ) {}

    public record Response() {}

}
