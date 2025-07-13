package com.codeit.project.slid_todo.application.studyManage.business.service;

import com.codeit.project.slid_todo.domain.study.business.service.StudyService;
import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
import com.codeit.project.slid_todo.domain.studyUser.business.service.StudyUserService;
import com.codeit.project.slid_todo.domain.user.business.service.UserService;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyManageFacade {

    private final UserService userService;
    private final StudyService studyService;
    private final StudyUserService studyUserService;


    public void createStudy(Long userId) {
        User user = userService.findUserById(userId);
        Study study = studyService.createStudy(user);
        studyUserService.save(user, study);
    }
}
