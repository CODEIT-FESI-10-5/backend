package com.codeit.project.slid_todo.domain.user.persistent.entity;

import com.codeit.project.slid_todo.common.audting.BaseDateTime;
import com.codeit.project.slid_todo.common.util.CodeGenerator;
import com.codeit.project.slid_todo.common.vo.UploadImg;
import com.codeit.project.slid_todo.domain.refreshToken.persistent.entity.RefreshToken;
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
public class User extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Column(unique = true)
    private String nickname;

    private UploadImg img;

    private boolean isDeleted = Boolean.FALSE;

    @OneToMany(mappedBy = "user")
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<StudyUser> studyUsers = new ArrayList<>();

    @Builder
    public User(List<RefreshToken> refreshTokens, String email, String password, String name) {
        this.refreshTokens = refreshTokens;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = generateDefaultNickname();
    }

    private String generateDefaultNickname() {
        return "user_" + CodeGenerator.generate(6);
    }

    public boolean hasCustomImage() {
        return this.img != null &&
                this.img.getStoreImgDir() != null &&
                !this.img.getStoreImgDir().isBlank();
    }

    public void updateProfile(String nickname, UploadImg img) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        this.img = img;
    }

    public void updatePassword(String encodedNewPassword) {
        if(encodedNewPassword != null) {
            this.password = encodedNewPassword;
        }
    }

    public void deleteUser() {
        this.isDeleted = Boolean.TRUE;
    }

}
