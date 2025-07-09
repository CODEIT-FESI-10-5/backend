package com.codeit.project.slid_todo.application.auth.business;

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
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthFacade {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;
    private final CookieUtils cookieUtils;

    public void registerUser(SignupDto.Request requestDto) {
        userService.registerUser(
                requestDto.name(),
                requestDto.email(),
                requestDto.password()
        );
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
            response.addHeader(HttpHeaders.AUTHORIZATION, JwtProperties.TOKEN_PREFIX + newAccess);
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
                user.getId(),
                user.getUserRole()
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
