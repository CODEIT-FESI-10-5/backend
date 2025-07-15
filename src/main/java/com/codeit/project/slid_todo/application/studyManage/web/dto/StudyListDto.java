package com.codeit.project.slid_todo.application.studyManage.web.dto;

import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
import lombok.Builder;

import java.util.List;

public record StudyListDto() {

    public record Request() {

    }

    public record Response(
            int totalCount,
            Long recentStudyId,
            List<StudyInfo> studyList
    ) {

        @Builder
        private record StudyInfo(
                Long studyId,
                String title,
                String description
        ) {

        }

        public static Response from(List<StudyUser> studyUserList) {
            List<StudyInfo> studyInfoList = studyUserList.stream()
                    .map(studyUser -> StudyInfo.builder()
                            .studyId(studyUser.getStudy().getId())
                            .title(studyUser.getStudy().getTitle())
                            .description(studyUser.getStudy().getDescription())
                            .build()
                    )
                    .toList();

            Long recentStudyId = studyUserList.isEmpty()
                    ? null
                    : studyUserList.get(0).getStudy().getId();

            return new Response(studyInfoList.size(), recentStudyId, studyInfoList);
        }
    }
}
