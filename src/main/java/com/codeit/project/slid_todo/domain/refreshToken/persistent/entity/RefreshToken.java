package com.codeit.project.slid_todo.domain.refreshToken.persistent.entity;

import com.codeit.project.slid_todo.common.audting.BaseDateTime;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String token;

    private Date expiryAt;

    @Builder
    public RefreshToken(User user, String token, Date expiryAt) {
        addUser(user);
        this.user = user;
        this.token = token;
        this.expiryAt = expiryAt;
    }

    private void addUser(User user) {
        if (user != null) {
            this.user = user;
            user.getRefreshTokens().add(this);
        }
    }

    public void updateRefreshToken(String token, Date expiryAt) {
        this.token = token;
        this.expiryAt = expiryAt;
    }
}
