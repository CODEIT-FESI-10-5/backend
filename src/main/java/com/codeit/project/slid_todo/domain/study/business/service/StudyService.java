package com.codeit.project.slid_todo.domain.study.business.service;

import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
import com.codeit.project.slid_todo.domain.study.persistent.repository.DomainStudyRepository;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class StudyService {

    private final DomainStudyRepository studyRepository;

    public Study createStudy(User user) {
        Study study = Study.builder().build();
        studyRepository.save(study);
        return study;
    }
}
