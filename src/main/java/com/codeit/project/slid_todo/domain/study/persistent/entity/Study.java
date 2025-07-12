package com.codeit.project.slid_todo.domain.study.persistent.entity;

import com.codeit.project.slid_todo.common.audting.BaseDateTime;
import com.codeit.project.slid_todo.domain.studyUser.persistent.entity.StudyUser;
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
public class Study extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String image;

    private String inviteCode;

    private boolean isDeleted = false;

    @OneToMany(mappedBy = "study")
    private List<StudyUser> studyUsers = new ArrayList<>();

    @Builder
    public Study(String title, String description, String image, String inviteCode) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.inviteCode = inviteCode;
    }

}
