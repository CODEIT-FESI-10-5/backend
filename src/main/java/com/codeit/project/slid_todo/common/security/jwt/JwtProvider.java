package com.codeit.project.slid_todo.common.security.jwt;

import com.codeit.project.slid_todo.domain.user.persistent.entity.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    private Key key;

    public JwtProvider(@Value("${spring.jwt.secret}") String secret) {
        byte[] byteSecretKey = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(byteSecretKey);
    }

    public String generateAccessToken(String subject, Long id, UserRole userRole) {
        long now = new Date().getTime();

        return Jwts.builder()
                .setSubject(subject)
                .claim("id", id)
                .claim(JwtProperties.AUTHORITIES_KEY, userRole.getRole())
                .setExpiration(new Date(now + JwtProperties.ACCESS_EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String subject) {
        long now = new Date().getTime();

        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(now + JwtProperties.REFRESH_EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
