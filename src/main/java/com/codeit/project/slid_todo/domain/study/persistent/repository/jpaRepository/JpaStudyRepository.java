package com.codeit.project.slid_todo.domain.study.persistent.repository.jpaRepository;

import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface JpaStudyRepository extends JpaRepository<Study, Long> {
    boolean existsByInviteCodeAndIsDeletedFalse(String code);

    Optional<Study> findByInviteCode(String inviteCode);

    Study findByTitle(String title);
}
