package ru.ershov.project.orderservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenAPIConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public OpenAPI openAPI() {
        ModelConverters.getInstance().addConverter(new ModelResolver(objectMapper));
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createApiKeyScheme()))
                .info(new Info().title("API сервиса заказов")
                        .description("В данном разделе храняться методы для order-service и вся документация по ним")
                        .version("0.0.1")
                        .contact(new Contact().name("Vladimir Ershov").email("secamm66@mail.ru"))
                        .license(new License().name("License of API").url("API license URL")));

    }

    @Bean
    public GroupedOpenApi customOpenApi() {
        return GroupedOpenApi.builder()
                .group("order-service")
                .pathsToMatch("/api/v1/orders/**")
                .addOpenApiCustomiser(openApi -> openApi
                        .servers(List.of(new Server().url("http://localhost:8080/"))))
                .build();
    }

    private SecurityScheme createApiKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer");
    }
}