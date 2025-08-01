package com.example.finalprojectcoursemanagementsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Course Management API")
                        .version("1.0.0")
                        .description("This API allows managing users, courses, roles, and more.")
                        .contact(new Contact().email("ejafarov20416@ada.edu.az")
                                .name("Emil Jafarov")));
    }

}
