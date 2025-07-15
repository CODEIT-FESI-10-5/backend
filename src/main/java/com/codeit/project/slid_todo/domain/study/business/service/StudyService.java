package com.codeit.project.slid_todo.domain.study.business.service;

import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.common.util.ImgStore;
import com.codeit.project.slid_todo.common.util.InviteCodeGenerator;
import com.codeit.project.slid_todo.common.vo.UploadImg;
import com.codeit.project.slid_todo.domain.study.errorCode.StudyErrorCode;
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
    private final ImgStore imgStore;

    public Study createStudy(User user) {
        String inviteCode = generateUniqueInviteCode();

        Study study = Study.builder()
                .inviteCode(inviteCode)
                .build();

        return studyRepository.save(study);
    }

     private String generateUniqueInviteCode() {
        String code;
        do {
            code = InviteCodeGenerator.generate();
        } while (studyRepository.existsByInviteCode(code));
        return code;
    }

    public void updateStudy(Long studyId, String title, String description, UploadImg uploadImg) {
        Study study = studyRepository.getByIdOrThrow(studyId);

        if (uploadImg != null && study.getImage() != null) {
            imgStore.deleteImage(study.getImage().getStoreImgDir());
        }

        study.updateStudy(title, description, uploadImg);
    }

    public Study getStudyIfInviteCodeMatches(Long studyId, String inputCode) {
        Study study = studyRepository.getByIdOrThrow(studyId);
        if (!study.getInviteCode().equals(inputCode)) {
            throw new BaseException(StudyErrorCode.INVALID_INVITE_CODE);
        }
        return study;
    }

    public Study findByStudyId(Long studyId) {
        return studyRepository.getByIdOrThrow(studyId);
    }
}
