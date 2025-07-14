package com.codeit.project.slid_todo.common.aspect;

import com.codeit.project.slid_todo.common.annotation.CheckStudyLeader;
import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.domain.studyUser.business.service.StudyUserService;
import com.codeit.project.slid_todo.domain.studyUser.errorCode.StudyUserErrorCode;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class StudyLeaderCheckAspect {

    private final StudyUserService studyUserService;

    @Before("@annotation(checkStudyLeader)")
    public void checkUserRole(JoinPoint joinPoint, CheckStudyLeader checkStudyLeader) {
        String studyIdParamName = checkStudyLeader.studyIdParam();
        String userIdParamName = checkStudyLeader.userIdParam();

        Long studyId = extractParam(joinPoint, studyIdParamName);
        Long userId  = extractParam(joinPoint, userIdParamName);

        StudyUser studyUser = studyUserService.getOrThrowIfNotJoined(studyId, userId);

        if(studyUser.getUserRole() != UserRole.LEADER) {
            throw new BaseException(StudyUserErrorCode.NOT_STUDY_LEADER);
        }

    }

    private Long extractParam(JoinPoint joinPoint, String targetParamName) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramsName = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < paramsName.length; i++) {
            if(paramsName[i].equals(targetParamName) && args[i] instanceof Long) {
                return (Long) args[i];
            }
        }

        throw new IllegalArgumentException("파라미터를 찾을 수 없습니다: " + targetParamName);
    }

}
