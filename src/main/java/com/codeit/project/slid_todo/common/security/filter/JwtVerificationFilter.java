package com.codeit.project.slid_todo.common.security.filter;

import com.codeit.project.slid_todo.common.security.errorCode.AuthErrorCode;
import com.codeit.project.slid_todo.common.security.jwt.JwtProperties;
import com.codeit.project.slid_todo.common.security.jwt.JwtProvider;
import com.codeit.project.slid_todo.common.security.vo.CustomUserDetails;
import com.codeit.project.slid_todo.common.util.CookieUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final CookieUtils cookieUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            setAuthenticationToContext(request);
        } catch (ExpiredJwtException ee) {
            request.setAttribute("errorCode", AuthErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (SignatureException se) {
            request.setAttribute("errorCode", AuthErrorCode.INVALID_SIGNATURE_ACCESS_TOKEN);
        } catch (JwtException je) {
            request.setAttribute("errorCode", AuthErrorCode.UNAUTHENTICATED);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !validAuthorizationHeader(request);
    }

    private boolean validAuthorizationHeader(HttpServletRequest request) {
        String authorizationHeader = getAuthenticationTokenToCookie(request);
//        return authorizationHeader != null && authorizationHeader.startsWith(jwtProperties.getTokenPrefix());
        return authorizationHeader != null;
    }

    private void setAuthenticationToContext(HttpServletRequest request) {
        SecurityContextHolder.getContext().setAuthentication(createAuthenticatedToken(request));
    }

    private Authentication createAuthenticatedToken(HttpServletRequest request) {
        CustomUserDetails userDetails = createUserDetails(request);
        return new UsernamePasswordAuthenticationToken(userDetails.getUserIdx(), null, userDetails.getAuthorities());
    }

    private CustomUserDetails createUserDetails(HttpServletRequest request) {
//        String token = getAuthenticationTokenToCookie(request).substring(jwtProperties.getTokenPrefix().length());
        String token = getAuthenticationTokenToCookie(request);
        return new CustomUserDetails(jwtProvider.getClaims(token));
    }

    private String getAuthenticationTokenToCookie(HttpServletRequest request) {
        return cookieUtils.extractAccessToken(request);
    }

}
