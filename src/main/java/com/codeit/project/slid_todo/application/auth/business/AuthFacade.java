package com.codeit.project.slid_todo.application.auth.business;

import com.codeit.project.slid_todo.application.auth.web.dto.LoginRequestDto;
import com.codeit.project.slid_todo.application.auth.web.dto.LoginResponseDto;
import com.codeit.project.slid_todo.application.auth.web.dto.SignupDto;
import com.codeit.project.slid_todo.common.security.dto.RefreshTokenDto;
import com.codeit.project.slid_todo.common.security.errorCode.AuthErrorCode;
import com.codeit.project.slid_todo.common.security.jwt.JwtProperties;
import com.codeit.project.slid_todo.common.security.jwt.JwtProvider;
import com.codeit.project.slid_todo.common.util.CookieUtils;
import com.codeit.project.slid_todo.domain.refreshToken.business.service.RefreshTokenService;
import com.codeit.project.slid_todo.domain.user.business.service.UserService;
import com.codeit.project.slid_todo.domain.user.persistent.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthFacade {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtProperties jwtProperties;
    private final JwtProvider jwtProvider;
    private final CookieUtils cookieUtils;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(SignupDto.Request requestDto) {
        userService.registerUser(
                requestDto.name(),
                requestDto.email(),
                requestDto.password()
        );
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userService.findByEmail(requestDto.email());
        
        // 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.password(), user.getPassword())) {
            throw new RuntimeException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        
        // JWT 토큰 생성
        String accessToken = jwtProvider.generateAccessToken(user.getEmail(), user.getId());
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
        Date refreshTokenExpiry = jwtProvider.getClaims(refreshToken).getExpiration();
        
        // Refresh Token 저장
        refreshTokenService.registerRefreshToken(user, refreshToken, refreshTokenExpiry);
        
        return LoginResponseDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void registerRefreshToken(RefreshTokenDto refreshTokenDto) {
        User user = userService.findUserById(refreshTokenDto.userId());
        refreshTokenService.registerRefreshToken(
                user,
                refreshTokenDto.refreshToken(),
                refreshTokenDto.expiryAt()
        );
    }

    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        try {
            String preRefresh = cookieUtils.extractRefreshToken(request);

            User user = validRefreshTokenSubject(preRefresh);

            String newRefresh = generateRefreshToken(preRefresh);
            String newAccess = getAccessToken(user);

            updateRefreshToken(newRefresh, preRefresh, user);

            response.setCharacterEncoding("utf-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            response.addHeader(HttpHeaders.AUTHORIZATION, jwtProperties.getTokenPrefix() + newAccess);
            response.addCookie(cookieUtils.createCookie("refreshToken", newRefresh));
        } catch (ExpiredJwtException ee) {
            request.setAttribute("errorCode", AuthErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (SignatureException se) {
            request.setAttribute("errorCode", AuthErrorCode.INVALID_SIGNATURE_ACCESS_TOKEN);
        } catch (JwtException je) {
            request.setAttribute("errorCode", AuthErrorCode.UNAUTHENTICATED);
        }
    }

    private void updateRefreshToken(String newRefresh, String preRefresh, User user) {
        Date newExpiryAt = jwtProvider.getClaims(newRefresh).getExpiration();
        refreshTokenService.updateRefresh(user, newRefresh, preRefresh, newExpiryAt);
    }

    private String getAccessToken(User user) {
        return jwtProvider.generateAccessToken(
                user.getEmail(),
                user.getId()
        );
    }

    private String generateRefreshToken(String refreshToken) {
        return jwtProvider.generateRefreshToken(refreshToken);
    }

    private User validRefreshTokenSubject(String refreshToken) {
        String email = findUserInfo(refreshToken);
        return userService.findByEmail(email);
    }

    private String findUserInfo(String refreshToken) {
        return jwtProvider.getClaims(refreshToken).getSubject();
    }

}
