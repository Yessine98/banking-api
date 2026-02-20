package com.yassine.bankingapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Banking API")
                        .version("1.0.0")
                        .description("RESTful API for Banking Operations - Portfolio Project\n\n" +
                                "## Features\n" +
                                "- Customer Management (CRUD)\n" +
                                "- Account Management\n" +
                                "- Banking Transactions (Deposit, Withdrawal, Transfer)\n" +
                                "- JWT Authentication\n" +
                                "- Role-based Access Control (USER, ADMIN)\n\n" +
                                "## Authentication\n" +
                                "1. Register a new user via `/api/auth/register`\n" +
                                "2. Login via `/api/auth/login` to get a JWT token\n" +
                                "3. Click 'Authorize' and enter: `Bearer <your_token>`")
                        .contact(new Contact()
                                .name("Yassine Ben Rejeb")
                                .email("benrejeb98@gmail.com")
                                .url("https://github.com/bryessine"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token obtained from /api/auth/login")));
    }
}
