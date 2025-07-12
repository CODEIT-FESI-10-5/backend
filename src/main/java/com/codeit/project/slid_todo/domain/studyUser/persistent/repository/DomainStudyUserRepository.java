package com.codeit.project.slid_todo.domain.studyUser.persistent.repository;

import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.domain.studyUser.errorCode.StudyUserErrorCode;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
import com.codeit.project.slid_todo.domain.studyUser.persistent.repository.jpaRepository.JpaStudyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DomainStudyUserRepository {

    private final JpaStudyUserRepository jpaStudyUserRepository;

    public void save(StudyUser studyUser) {
        jpaStudyUserRepository.save(studyUser);
    }

    public StudyUser getOrThrowIfNotJoined(Long studyId, Long userId) {
        return jpaStudyUserRepository.findByStudyIdAndUserId(studyId, userId)
                .orElseThrow(() -> new BaseException(StudyUserErrorCode.STUDY_USER_NOT_FOUND));
    }
}
