package com.codeit.project.slid_todo.common.security.jwt;

public record JwtPayload (
        Long id,
        String email
) {
}
