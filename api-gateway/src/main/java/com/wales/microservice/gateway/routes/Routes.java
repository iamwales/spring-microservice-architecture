package com.wales.microservice.gateway.routes;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.*;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
public class Routes {

    @Bean
    public RouterFunction<ServerResponse> productsServiceRoutes() {
        return route("product_service")
                .route(
                        // the RequestPredicates has different methods like headers path, GET...
                        // it routes every header or path given to the required url/microservice.
                        RequestPredicates.path("/api/v1/products"),
                        HandlerFunctions.http("http://localhost:8080"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("productServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productsServiceRoutesApiDocs() {
        return route("product_service_swagger")
                .route(
                        RequestPredicates.path("/aggregate/product-service/v3/api-docs"),
                        HandlerFunctions.http("http://localhost:8080")).filter(setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("productServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoutes() {
        return route("order_service")
                .route(
                        RequestPredicates.path("/api/v1/orders"),
                        HandlerFunctions.http("http://localhost:8081"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoutesApiDocs() {
        return route("order_service_swagger")
                .route(
                        RequestPredicates.path("/aggregate/order-service/v3/api-docs"),
                        HandlerFunctions.http("http://localhost:8081")).filter(setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoriesServiceRoutes() {
        return route("inventory_service")
                .route(
                        RequestPredicates.path("/api/v1/inventories"),
                        HandlerFunctions.http("http://localhost:8082"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoutesApiDocs() {
        return route("inventory_service_swagger")
                .route(
                        RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"),
                        HandlerFunctions.http("http://localhost:8082")).filter(setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallBackRoute() {
        return route("fallbackRoute").GET("/fallbackRoute",
                request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service Unavailable, please try again later"))
                .build();
    }
}
