package com.codeit.project.slid_todo.domain.user.persistent.entity;

import com.codeit.project.slid_todo.common.audting.BaseDateTime;
import com.codeit.project.slid_todo.domain.refreshToken.persistent.entity.RefreshToken;
import com.codeit.project.slid_todo.domain.user.persistent.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private boolean is_deleted = Boolean.FALSE;

    @Builder
    public User(List<RefreshToken> refreshTokens, String email, String password, String name, UserRole userRole) {
        this.refreshTokens = refreshTokens;
        this.email = email;
        this.password = password;
        this.name = name;
        this.userRole = userRole;
    }
}
