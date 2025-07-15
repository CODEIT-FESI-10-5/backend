package com.codeit.project.slid_todo.domain.studyUser.persistent.repository;

import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.domain.studyUser.errorCode.StudyUserErrorCode;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
import com.codeit.project.slid_todo.domain.studyUser.persistent.repository.jpaRepository.JpaStudyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public StudyUser findByStudyIdAndUserId(Long studyId, Long userId) {
        return jpaStudyUserRepository.findByStudyIdAndUserId(studyId, userId).orElse(null);
    }

    public StudyUser getByIdOrThrow(Long studyUserId) {
        return jpaStudyUserRepository.findById(studyUserId)
                .orElseThrow(() -> new BaseException(StudyUserErrorCode.STUDY_USER_NOT_FOUND));
    }

    public List<StudyUser> findByStudyId(Long studyId) {
        return jpaStudyUserRepository.findByStudyId(studyId);
    }

    public List<StudyUser> findAllWithStudyByUserId(Long userId) {
        return jpaStudyUserRepository.findByUserIdAndIsDeletedFalse(userId);
    }
}
