package com.codeit.project.slid_todo.application.studyManage.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record EditStudyInfoDto() {

    @Schema(description = "스터디 정보 수정 요청")
    public record Request(
            @Schema(description = "스터디 제목", example = "스프링 부트 스터디")
            String title,

            @Schema(description = "스터디 설명", example = "스프링 부트를 함께 공부하는 스터디입니다.")
            String description
    ) {}

    public record Response() {}

}
