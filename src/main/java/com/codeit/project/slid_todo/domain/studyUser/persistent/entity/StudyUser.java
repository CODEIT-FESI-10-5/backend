package com.codeit.project.slid_todo.domain.studyUser.persistent.entity;

import com.codeit.project.slid_todo.common.audting.BaseDateTime;
import com.codeit.project.slid_todo.domain.study.persistent.entity.Study;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.enums.UserRole;
import com.codeit.project.slid_todo.domain.todo.persistent.entity.Todo;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyUser extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "assignedUser")
    private List<Todo> todoList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private boolean isDeleted = false;

    @Builder
    public StudyUser(Study study, User user, UserRole userRole) {
        addStudy(study);
        addUser(user);
        this.userRole = userRole;
    }

    private void addStudy(Study study) {
        if (study != null) {
            this.study = study;
            study.getStudyUsers().add(this);
        }
    }

    private void addUser(User user) {
        if (user != null) {
            this.user = user;
            user.getStudyUsers().add(this);
        }
    }
}
