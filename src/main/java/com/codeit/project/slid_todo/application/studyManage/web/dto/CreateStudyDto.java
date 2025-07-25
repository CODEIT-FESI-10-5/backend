package com.codeit.project.slid_todo.application.studyManage.web.dto;

import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;

import java.util.List;

public record CreateStudyDto() {

    public record Request() {}

    public record Response(
            Long studyId
    ) {
        public static CreateStudyDto.Response from(Study study) {
            return new Response(study.getId());
        }
    }
}
