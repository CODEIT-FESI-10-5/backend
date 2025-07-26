package com.codeit.project.slid_todo.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Slid Todo API")
                        .description("""
                                ## 스터디 목표 관리 및 투두 리스트 API
                                
                                ### 주요 기능
                                - **투두 관리**: 투두 생성, 수정, 조회, 삭제
                                - **목표 관리**: 목표 생성, 수정, 삭제, 우선순위 관리
                                - **노트 관리**: 투두별 노트 작성 및 관리
                                - **스터디 관리**: 스터디 생성, 참가, 정보 수정
                                - **대시보드**: 팀 진행도 및 개인 진행도 조회
                                - **사용자 관리**: 프로필 수정, 회원가입
                                
                                ### 인증
                                - JWT 토큰 기반 인증
                                - Access Token과 Refresh Token 사용
                                
                                ### 사용법
                                1. 회원가입 후 로그인하여 토큰 발급
                                2. 스터디 생성 또는 참가
                                3. 목표 설정 및 투두 생성
                                4. 진행도 확인 및 관리
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Slid Todo Team")
                                .email("contact@slidtodo.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("https://api.modudo.shop").description("Production Server"),
                        new Server().url("http://localhost:8080").description("Local Development Server")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", 
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT 토큰을 입력하세요. 'Bearer ' 접두사는 자동으로 추가됩니다.")));
    }
} 