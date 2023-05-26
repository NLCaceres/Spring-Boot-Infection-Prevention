package edu.usc.nlcaceres.infectionprotection_backend;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Instead of using SpringFox's Swagger UI directly (which doesn't support Spring Boot 3),
// I can use SpringDoc-OpenAPI to enable Spring Boot 3 support. The config API is slightly different but mostly just name changes
@Configuration
public class SpringDocConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/**")
                .build();
    }
}
