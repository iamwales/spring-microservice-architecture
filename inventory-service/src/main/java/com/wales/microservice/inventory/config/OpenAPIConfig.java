package com.wales.microservice.inventory.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI inventoryAPIDoc() {
        return new OpenAPI()
                .info(new Info().title("Inventory Service APIs").description(
                                "Rest Api for inventory microservice").version("v0.0.1")
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation().description("You can refer to the inventory service wiki documentation")
                        .url("https://inventory-service-dummy-url.com"));
    }
}
