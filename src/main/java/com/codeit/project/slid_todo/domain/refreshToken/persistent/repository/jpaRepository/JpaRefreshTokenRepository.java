package com.codeit.project.slid_todo.domain.refreshToken.persistent.repository.jpaRepository;

import com.codeit.project.slid_todo.domain.refreshToken.persistent.entity.RefreshToken;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface JpaRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserAndToken(User user, String refreshToken);
    void deleteByToken(String refreshToken);
}
