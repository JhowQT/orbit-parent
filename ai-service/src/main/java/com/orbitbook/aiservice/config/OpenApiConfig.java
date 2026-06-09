package com.orbitbook.aiservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("OrbitBook AI Service API")
                                .version("1.0")
                                .description(
                                        "Microserviço de recomendações com IA (RAG + Tooling + MCP via Google Gemini)."
                                )
                                .contact(
                                        new Contact()
                                                .name("OrbitBook Team")
                                                .email("orbitbook@fiap.com")
                                )
                );
    }
}
