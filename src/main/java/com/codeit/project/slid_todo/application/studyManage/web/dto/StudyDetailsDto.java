package com.codeit.project.slid_todo.application.studyManage.web.dto;

import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record StudyDetailsDto() {

    public record Request() {
    }

    @Builder
    public record Response(
            Long studyId,
            String title,
            String description,
            LocalDateTime createAt,
            String studyImageDir,
            String inviteCode,
            int teamProgress,
            List<Member> members
    ) {
        @Builder
        private record Member(
                Long userId,
                String nickname,
                String userImageDir
        ) {

        }

        public static StudyDetailsDto.Response from(Study study, List<StudyUser> studyUserList, int teamProgress) {

            List<Member> memberList = studyUserList.stream()
                    .map(studyUser -> Member.builder()
                            .userId(studyUser.getUser().getId())
                            .nickname(studyUser.getUser().getNickname())
                            .userImageDir(studyUser.getUser().getImg().getStoreImgDir())
                            .build())
                    .toList();

            return Response.builder()
                    .studyId(study.getId())
                    .title(study.getTitle())
                    .description(study.getDescription())
                    .createAt(study.getCreatedAt())
                    .studyImageDir(
                            study.getImage() != null ? study.getImage().getStoreImgDir() : null
                    )
                    .inviteCode(study.getInviteCode())
                    .teamProgress(teamProgress)
                    .members(memberList)
                    .build();
        }
    }
}
