package com.codeit.project.slid_todo.domain.refreshToken.persistent.repository;

import com.codeit.project.slid_todo.common.exception.BaseException;
import com.codeit.project.slid_todo.domain.refreshToken.errorCode.RefreshErrorCode;
import com.codeit.project.slid_todo.domain.refreshToken.persistent.entity.RefreshToken;
import com.codeit.project.slid_todo.domain.refreshToken.persistent.repository.jpaRepository.JpaRefreshTokenRepository;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DomainRefreshTokenRepository {

    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;

    public void save(RefreshToken refreshToken) {
        jpaRefreshTokenRepository.save(refreshToken);
    }

    public RefreshToken getValidRefreshTokenOrThrow(User user, String refreshToken) {
        return jpaRefreshTokenRepository.findByUserAndToken(user, refreshToken)
                .orElseThrow(() -> new BaseException(RefreshErrorCode.NOT_EXIST_REFRESH_TOKEN));
    }

    public void delete(String refreshToken) {
        jpaRefreshTokenRepository.deleteByToken(refreshToken);
    }
}
