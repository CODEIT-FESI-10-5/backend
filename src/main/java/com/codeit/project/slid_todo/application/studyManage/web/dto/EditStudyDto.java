package com.codeit.project.slid_todo.application.studyManage.web.dto;

import org.springframework.web.multipart.MultipartFile;

public record EditStudyDto() {

    public record Request(
            String title,

            String description,

            MultipartFile image
    ) {
    }

    public record Response() {
    }

}
