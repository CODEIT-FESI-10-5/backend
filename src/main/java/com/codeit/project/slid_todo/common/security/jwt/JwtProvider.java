package com.codeit.project.slid_todo.common.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    private Key key;
    private JwtProperties jwtProperties;

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        byte[] byteSecretKey = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        key = Keys.hmacShaKeyFor(byteSecretKey);
    }

    public String generateAccessToken(String subject, Long id) {
        long now = new Date().getTime();

        return Jwts.builder()
                .setSubject(subject)
                .claim("id", id)
                .setExpiration(new Date(now + jwtProperties.getAccessExpirationTime()))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String subject) {
        long now = new Date().getTime();

        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(now + jwtProperties.getRefreshExpirationTime()))
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
