package com.codeit.project.slid_todo.domain.studyUser.persistent.repository.jpaRepository;

import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaStudyUserRepository extends JpaRepository<StudyUser, Long> {
    Optional<StudyUser> findByStudyIdAndUserId(Long studyId, Long userId);
}
