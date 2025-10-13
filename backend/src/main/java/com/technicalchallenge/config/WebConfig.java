package com.technicalchallenge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    
    @Value("${management.endpoints.web.cors.allowed-origins}")
    private String allowedOrigins;
    
    @Value("${management.endpoints.web.cors.allowed-methods}")
    private String allowedMethods;
    
    @Value("${management.endpoints.web.cors.allowed-headers}")
    private String allowedHeaders;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(allowedOrigins.split(","))
                        .allowedMethods(allowedMethods.split(","))
                        .allowedHeaders(allowedHeaders)
                        .allowCredentials(true);
            }
        };
    }
}

