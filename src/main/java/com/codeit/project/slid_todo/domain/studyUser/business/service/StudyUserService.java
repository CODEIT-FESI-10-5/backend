package com.codeit.project.slid_todo.domain.studyUser.business.service;

import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.enums.UserRole;
import com.codeit.project.slid_todo.domain.studyUser.persistent.repository.DomainStudyUserRepository;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class StudyUserService {

    private final DomainStudyUserRepository studyUserRepository;

    public void save(User user, Study study) {
        StudyUser studyUser = StudyUser.builder()
                .study(study)
                .user(user)
                .userRole(UserRole.LEADER)
                .build();

        studyUserRepository.save(studyUser);
    }
}
