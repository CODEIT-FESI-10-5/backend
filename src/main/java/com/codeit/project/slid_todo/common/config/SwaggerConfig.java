package com.codeit.project.slid_todo.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
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
                        .description("스터디 목표 관리 및 투두 리스트 API")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Slid Todo Team")
                                .email("contact@slidtodo.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Server"),
                        new Server().url("https://api.slidtodo.com").description("Production Server")
                ));
    }
} 