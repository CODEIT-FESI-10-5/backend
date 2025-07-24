package com.codeit.project.slid_todo.domain.study.persistent.repository;

import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.domain.study.errorCode.StudyErrorCode;
import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
import com.codeit.project.slid_todo.domain.study.persistent.repository.jpaRepository.JpaStudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DomainStudyRepository {

    private final JpaStudyRepository jpaStudyRepository;

    public Study getByIdOrThrow(Long studyId) {
        return jpaStudyRepository.findById(studyId)
                .orElseThrow(() -> new BaseException(StudyErrorCode.NOT_EXIST_STUDY));
    }

    public Study getByInviteCodeOrThrow(String inviteCode) {
        return jpaStudyRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new BaseException(StudyErrorCode.INVALID_INVITE_CODE));
    }


    public Study save(Study study) {
        return jpaStudyRepository.save(study);
    }

    public boolean existsByInviteCode(String code) {
        return jpaStudyRepository.existsByInviteCodeAndIsDeletedFalse(code);
    }

    public Study findByTitle(String title) {
        return jpaStudyRepository.findByTitle(title);
    }
}
