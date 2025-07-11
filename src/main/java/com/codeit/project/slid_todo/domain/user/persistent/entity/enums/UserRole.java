package com.codeit.project.slid_todo.domain.user.persistent.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum UserRole {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String role;

    public static UserRole fromRole(String roleStr) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.getRole().equals(roleStr))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown role: " + roleStr));
    }
}
