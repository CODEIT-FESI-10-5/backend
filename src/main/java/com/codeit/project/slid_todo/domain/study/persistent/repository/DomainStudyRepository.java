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

    public void save(Study study) {
        jpaStudyRepository.save(study);
    }

    public Study getByIdOrThrow(Long id) {
        return jpaStudyRepository.findById(id)
                .orElseThrow(() -> new BaseException(StudyErrorCode.NOT_EXIST_STUDY));
    }
}
