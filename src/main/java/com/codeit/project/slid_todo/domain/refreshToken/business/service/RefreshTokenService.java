package com.codeit.project.slid_todo.domain.refreshToken.business.service;

import com.codeit.project.slid_todo.domain.refreshToken.persistent.entity.RefreshToken;
import com.codeit.project.slid_todo.domain.refreshToken.persistent.repository.DomainRefreshTokenRepository;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final DomainRefreshTokenRepository  refreshTokenRepository;

    public void registerRefreshToken(User user, String refreshToken, Date expires) {
        RefreshToken entity = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expiryAt(expires)
                .build();

        refreshTokenRepository.save(entity);
    }

    public void updateRefresh(User user, String newRefresh, String preRefresh, Date expiryAt) {
        RefreshToken refreshToken = refreshTokenRepository.getValidRefreshTokenOrThrow(user, preRefresh);
        refreshToken.updateRefreshToken(newRefresh, expiryAt);
    }

    public void delete(String refresh) {
        refreshTokenRepository.delete(refresh);
    }
}
