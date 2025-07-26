package com.codeit.project.slid_todo.common.config;

import com.codeit.project.slid_todo.common.security.filter.JwtAuthenticationFilter;
import com.codeit.project.slid_todo.common.security.filter.JwtVerificationFilter;
import com.codeit.project.slid_todo.common.security.handler.*;
import com.codeit.project.slid_todo.common.security.jwt.JwtProperties;
import com.codeit.project.slid_todo.common.security.jwt.JwtProvider;
import com.codeit.project.slid_todo.common.util.CookieUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationFailureHandler failureHandler;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final ObjectMapper objectMapper;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/user/signup",
            "/api/auth/login",
            "/api/login",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**"
    };

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           HttpSession httpSession,
                                           JwtProperties jwtProperties,
                                           JwtProvider jwtProvider,
                                           CookieUtils cookieUtils,
                                           AuthenticationManager authenticationManager,
                                           CustomAuthenticationSuccessHandler successHandler) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated());

        http
                .addFilterBefore(new JwtVerificationFilter(jwtProvider, jwtProperties, cookieUtils), JwtAuthenticationFilter.class)
                .addFilterAt(jwtAuthenticationFilter(authenticationManager, successHandler), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutSuccessHandler(logoutSuccessHandler).logoutUrl("/api/logout"))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }

    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                                           CustomAuthenticationSuccessHandler successHandler) {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(objectMapper);
        filter.setFilterProcessesUrl("/api/login");
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        return filter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://modudo.shop"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}