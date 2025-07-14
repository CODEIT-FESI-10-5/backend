package com.codeit.project.slid_todo.domain.study.persistent.entity;

import com.codeit.project.slid_todo.common.audting.BaseDateTime;
import com.codeit.project.slid_todo.common.vo.UploadImg;
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

    @Embedded
    private UploadImg image;

    private String inviteCode;

    private boolean isDeleted = false;

    @OneToMany(mappedBy = "study")
    private List<StudyUser> studyUsers = new ArrayList<>();

    @Builder
    public Study(String title, String description, UploadImg image, String inviteCode) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.inviteCode = inviteCode;
    }

    public void updateStudy(String title, String description, UploadImg uploadImg) {
        if(title != null) {
            this.title = title;
        }
        if(description != null) {
            this.description = description;
        }
        if(uploadImg != null) {
            this.image = uploadImg;
        }
    }
}
