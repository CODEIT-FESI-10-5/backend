package com.codeit.project.slid_todo.application.studyManage.business.service;

import com.codeit.project.slid_todo.application.studyManage.web.dto.EditStudyDto;
import com.codeit.project.slid_todo.application.studyManage.web.dto.JoinStudyDto;
import com.codeit.project.slid_todo.common.util.ImgStore;
import com.codeit.project.slid_todo.common.vo.UploadImg;
import com.codeit.project.slid_todo.domain.study.business.service.StudyService;
import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
import com.codeit.project.slid_todo.domain.studyUser.business.service.StudyUserService;
import com.codeit.project.slid_todo.domain.user.business.service.UserService;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyManageFacade {

    private final UserService userService;
    private final StudyService studyService;
    private final StudyUserService studyUserService;
    private final ImgStore imgStore;


    public void createStudy(Long userId) {
        User user = userService.findUserById(userId);
        Study study = studyService.createStudy(user);
        studyUserService.saveLeader(user, study);
    }

    public void updateStudy(EditStudyDto.Request editStudyDto, Long studyId) throws IOException {
        String title = editStudyDto.title();
        String description = editStudyDto.description();

        MultipartFile image = editStudyDto.image();
        UploadImg uploadImg = imgStore.storeImg(image);

        studyService.updateStudy(studyId, title, description, uploadImg);
    }

    public void joinStudy(JoinStudyDto.Request joinStudyDto, Long studyId, Long userId) {
        Study study = studyService.getStudyIfInviteCodeMatches(studyId, joinStudyDto.inviteCode());
        studyUserService.validateNotJoined(studyId, userId);

        User user = userService.findUserById(userId);
        studyUserService.saveTeamMember(user, study);
    }
}
