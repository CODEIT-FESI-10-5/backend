package com.codeit.project.slid_todo.common.security.vo;

import com.codeit.project.slid_todo.domain.user.persistent.entity.enums.UserRole;
import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Builder
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final UserRole userRole;

    public CustomUserDetails(Claims claims) {
        this.id = claims.get("id", Long.class);
        this.email = claims.getSubject();
        this.password = "";
        this.userRole = UserRole.fromRole(claims.get("auth", String.class));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                return userRole.getRole();
            }
        });

        return authorities;
    }

    public Long getUserIdx() {
        return id;
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
