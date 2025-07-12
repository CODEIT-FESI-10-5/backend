package com.codeit.project.slid_todo.domain.studyUser.persistent.repository.jpaRepository;

import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStudyUserRepository extends JpaRepository<StudyUser, Long> {
}
