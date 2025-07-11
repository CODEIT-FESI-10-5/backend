package com.codeit.project.slid_todo.common.security.dto;

import java.util.Date;

public record RefreshTokenDto (
        Long userId,
        String refreshToken,
        Date expiryAt
) {
}
