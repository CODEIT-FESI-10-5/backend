package com.codeit.project.slid_todo.domain.studyUser.persistent.repository.jpaRepository;

import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaStudyUserRepository extends JpaRepository<StudyUser, Long> {
    Optional<StudyUser> findByStudyIdAndUserId(Long studyId, Long userId);

    @Query("SELECT su FROM StudyUser su WHERE su.study.id = :studyId AND su.isDeleted = false")
    List<StudyUser> findByStudyId(@Param("studyId") Long studyId);

    @Query("""
                SELECT su FROM StudyUser su
                JOIN FETCH su.study s
                WHERE su.user.id = :userId
                  AND su.isDeleted = false
                ORDER BY s.updatedAt DESC
            """)
    List<StudyUser> findByUserIdAndIsDeletedFalse(Long userId);

    @EntityGraph(attributePaths = {"user"})
    List<StudyUser> findByStudyIdAndIsDeletedFalse(Long studyId);
}
