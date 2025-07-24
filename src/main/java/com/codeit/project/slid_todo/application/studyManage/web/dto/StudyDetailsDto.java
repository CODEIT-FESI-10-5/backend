package com.codeit.project.slid_todo.application.studyManage.web.dto;

import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.enums.UserRole;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record StudyDetailsDto() {

    public record Request() {
    }

    @Builder
    public record Response(
            Long studyId,
            UserRole userRole,
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

        public static StudyDetailsDto.Response from(Study study, UserRole userRole, List<StudyUser> studyUserList, int teamProgress) {

            List<Member> memberList = studyUserList.stream()
                    .map(studyUser -> Member.builder()
                            .userId(studyUser.getUser().getId())
                            .nickname(studyUser.getUser().getNickname())
                            .userImageDir(
                                    studyUser.getUser().getImg() != null
                                            ? studyUser.getUser().getImg().getStoreImgDir()
                                            : null
                            )
                            .build())
                    .toList();

            return Response.builder()
                    .studyId(study.getId())
                    .userRole(userRole)
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
