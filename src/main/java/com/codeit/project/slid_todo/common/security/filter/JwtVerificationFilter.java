package com.codeit.project.slid_todo.common.security.filter;

import com.codeit.project.slid_todo.common.security.errorCode.AuthErrorCode;
import com.codeit.project.slid_todo.common.security.jwt.JwtProperties;
import com.codeit.project.slid_todo.common.security.jwt.JwtProvider;
import com.codeit.project.slid_todo.common.security.vo.CustomUserDetails;
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
        String authorizationHeader = getAuthenticationTokenToHeader(request);
        return authorizationHeader != null && authorizationHeader.startsWith(jwtProperties.getTokenPrefix());
    }

    private void setAuthenticationToContext(HttpServletRequest request) {
        SecurityContextHolder.getContext().setAuthentication(createAuthenticatedToken(request));
    }

    private Authentication createAuthenticatedToken(HttpServletRequest request) {
        CustomUserDetails userDetails = createUserDetails(request);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private CustomUserDetails createUserDetails(HttpServletRequest request) {
        String token = getAuthenticationTokenToHeader(request).substring(jwtProperties.getTokenPrefix().length());
        return new CustomUserDetails(jwtProvider.getClaims(token));
    }

    private String getAuthenticationTokenToHeader(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

}
