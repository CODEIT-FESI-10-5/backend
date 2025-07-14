package com.codeit.project.slid_todo.domain.studyUser.business.service;

import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
import com.codeit.project.slid_todo.domain.studyUser.errorCode.StudyUserErrorCode;
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

    public void saveLeader(User user, Study study) {
        StudyUser studyUser = StudyUser.builder()
                .study(study)
                .user(user)
                .userRole(UserRole.LEADER)
                .build();

        studyUserRepository.save(studyUser);
    }

    public void saveTeamMember(User user, Study study) {
        StudyUser studyUser = StudyUser.builder()
                .study(study)
                .user(user)
                .userRole(UserRole.NORMARL)
                .build();

        studyUserRepository.save(studyUser);
    }

    public StudyUser getOrThrowIfNotJoined(Long studyId, Long userId) {
        return studyUserRepository.getOrThrowIfNotJoined(studyId, userId);
    }

    public StudyUser findByStudyIdAndUserId(Long studyId, Long userId) {
        return studyUserRepository.findByStudyIdAndUserId(studyId, userId);
    }

    public void validateNotJoined(Long studyId, Long userId) {
        StudyUser studyUser = findByStudyIdAndUserId(studyId, userId);
        if (studyUser != null) {
            throw new BaseException(StudyUserErrorCode.ALREADY_JOINED);
        }
    }
}
