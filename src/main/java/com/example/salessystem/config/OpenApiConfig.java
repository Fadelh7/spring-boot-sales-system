package com.example.salessystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Sales System API", version = "v1", description = "REST API for managing products, clients, sales and transactions")
)
public class OpenApiConfig {}
