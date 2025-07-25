package com.codeit.project.slid_todo.application.studyManage.business.service;

import com.codeit.project.slid_todo.application.studyManage.web.dto.*;
import com.codeit.project.slid_todo.common.util.ImgStore;
import com.codeit.project.slid_todo.common.vo.UploadImg;
import com.codeit.project.slid_todo.domain.study.business.service.StudyService;
import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
import com.codeit.project.slid_todo.domain.studyUser.business.service.StudyUserService;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.enums.UserRole;
import com.codeit.project.slid_todo.domain.todo.business.service.TodoService;
import com.codeit.project.slid_todo.domain.user.business.service.UserService;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyManageFacade {

    private final UserService userService;
    private final StudyService studyService;
    private final StudyUserService studyUserService;
    private final TodoService todoService;
    private final ImgStore imgStore;

    @Transactional
    public CreateStudyDto.Response createStudy(Long userId) {
        User user = userService.findUserById(userId);
        Study study = studyService.createStudy(user);
        studyUserService.saveLeader(user, study);
        return CreateStudyDto.Response.from(study);
    }

    @Transactional
    public void updateStudyInfo(EditStudyInfoDto.Request editStudyInfoDto, Long studyId) throws IOException {
        String title = editStudyInfoDto.title();
        String description = editStudyInfoDto.description();

        studyService.updateStudyInfo(studyId, title, description);
    }

    @Transactional
    public void updateStudyImage(EditStudyImageDto.Request editStudyImageDto, Long studyId) throws IOException {
        MultipartFile image = editStudyImageDto.image();
        UploadImg uploadImg = imgStore.storeImg(image);

        studyService.updateStudyImage(studyId, uploadImg);
    }

    @Transactional
    public JoinStudyDto.Response joinStudy(JoinStudyDto.Request joinStudyDto, Long userId) {
        Study study = studyService.findByInviteCode(joinStudyDto.inviteCode());
        studyUserService.validateNotJoined(study.getId(), userId);

        User user = userService.findUserById(userId);
        studyUserService.saveTeamMember(user, study);

        return JoinStudyDto.Response.from(study);
    }

    public StudyListDto.Response getStudyList(Long userId) {
        List<StudyUser> studyUserList = studyUserService.findAllWithStudyByUserId(userId);
        return StudyListDto.Response.from(studyUserList);
    }

    public StudyDetailsDto.Response getStudyDetail(Long studyId, Long userId) {
        Study study = studyService.findByStudyId(studyId);
        List<StudyUser> studyUserList = studyUserService.findAllWithUserByStudyId(studyId);
        int totalProgress = calculateTeamTotalProgress(studyId);

        UserRole currentUserRole = studyUserList.stream()
                .filter(su -> su.getUser().getId().equals(userId))
                .map(StudyUser::getUserRole)
                .findFirst()
                .orElse(null);

        return StudyDetailsDto.Response.from(study, currentUserRole, studyUserList, totalProgress);
    }

    private int calculateTeamTotalProgress(Long studyId) {
        long completedCount = todoService.countCompletedByStudyId(studyId);
        long totalCount = todoService.countByStudy(studyId);

        return totalCount > 0 ? (int) ((completedCount * 100) / totalCount) : 0;
    }

    @Transactional
    public void deleteStudy(Long studyId, Long userId) {
        Study study = studyService.findByStudyId(studyId);
        StudyUser studyUser = studyUserService.getOrThrowIfNotJoined(studyId, userId);

        study.deleteStudy();
        studyUser.deleteStudyUser();
    }
}
