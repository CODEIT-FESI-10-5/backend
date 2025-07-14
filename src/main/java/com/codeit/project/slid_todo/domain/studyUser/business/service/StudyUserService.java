package com.codeit.project.slid_todo.domain.studyUser.business.service;

import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.enums.UserRole;
import com.codeit.project.slid_todo.domain.studyUser.persistent.repository.DomainStudyUserRepository;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyUserService {

    private final DomainStudyUserRepository studyUserRepository;

    public List<StudyUser> findByStudyId(Long studyId) {
        return studyUserRepository.findByStudyId(studyId);
    }

    public StudyUser getByIdOrThrow(Long studyUserId) {
        return studyUserRepository.getByIdOrThrow(studyUserId);
    }

    public StudyUser findByStudyIdAndUserId(Long studyId, Long userId) {
        return studyUserRepository.getOrThrowIfNotJoined(studyId, userId);
    }

    @Transactional
    public void save(User user, Study study) {
        StudyUser studyUser = StudyUser.builder()
                .study(study)
                .user(user)
                .userRole(UserRole.LEADER)
                .build();

        studyUserRepository.save(studyUser);
    }
}
