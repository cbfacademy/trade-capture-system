package com.technicalchallenge.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class OpenApiConfig {
    @Value("${springdoc.swagger-ui.server-url:http://localhost:8080}")
    private String devServerUrl;

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Trade Capture System API")
                        .version("1.0.0")
                        .description("Comprehensive API documentation for the Trade Capture System. " +
                                "This system manages trading operations including trade booking, counterparty management, " +
                                "user management, and cashflow generation.")
                        .contact(new Contact()
                                .name("Trade Capture System Team")
                                .email("support@tradecapture.com")
                                .url("https://github.com/trade-capture-system"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url(devServerUrl)
                                .description("Development server"),
                        new Server()
                                .url("https://api.tradecapture.com")
                                .description("Production server")))
                .tags(List.of(
                        new Tag().name("Trades")
                                .description("Trade management operations including booking, searching, and lifecycle management"),
                        new Tag().name("Trade Legs")
                                .description("Trade leg operations for managing individual legs of complex trades"),
                        new Tag().name("Cashflows")
                                .description("Cashflow generation and management for trades"),
                        new Tag().name("Users")
                                .description("User management and authentication operations"),
                        new Tag().name("Books")
                                .description("Trading book management for organizing trades by desk/strategy"),
                        new Tag().name("Counterparties")
                                .description("Counterparty management for trade settlement"),
                        new Tag().name("System")
                                .description("System monitoring and health check endpoints")
                ));
    }
}
