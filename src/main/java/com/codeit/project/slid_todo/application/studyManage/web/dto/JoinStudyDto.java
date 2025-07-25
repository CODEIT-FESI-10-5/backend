package com.codeit.project.slid_todo.application.studyManage.web.dto;

import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record JoinStudyDto() {

    @Schema(description = "스터디 참가 요청")
    public record Request(
            @NotNull
            @Schema(description = "초대 코드", example = "ABC123")
            String inviteCode
    ) {

    }

    public record Response(
            Long studyId
    ) {
        public static JoinStudyDto.Response from(Study study) {
            return new JoinStudyDto.Response(study.getId());
        }
    }
}
