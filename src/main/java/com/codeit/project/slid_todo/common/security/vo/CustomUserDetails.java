package com.codeit.project.slid_todo.common.security.vo;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;

    public CustomUserDetails(Claims claims) {
        this.id = claims.get("id", Long.class);
        this.email = claims.getSubject();
        this.password = "";
    }

    public Long getUserIdx() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

        @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
