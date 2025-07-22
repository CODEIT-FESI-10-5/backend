package com.codeit.project.slid_todo.application.studyManage.web.dto;

import org.springframework.web.multipart.MultipartFile;

public record EditStudyImageDto() {

    public record Request(
            MultipartFile image
    ) {
    }

    public record Response() {
    }

}
