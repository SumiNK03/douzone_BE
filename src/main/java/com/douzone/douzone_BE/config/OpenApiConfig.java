package com.douzone.douzone_BE.config;

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
                .info(new Info()
                        .title("Douzone BE API")
                        .version("v1")
                        .description("Douzone BE - 수민 인턴십 API 문서")
                        .contact(new Contact().name("Douzone Team").email("support@example.com"))
                );
    }
}
