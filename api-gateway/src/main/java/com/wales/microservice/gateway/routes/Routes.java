package com.wales.microservice.gateway.routes;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.*;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;

@Configuration
public class Routes {

    @Bean
    public RouterFunction<ServerResponse> productsServiceRoutes() {
        return GatewayRouterFunctions.route("product_service")
                .route(
                        // the RequestPredicates has different methods like headers path, GET...
                        // it routes every header or path given to the required url/microservice.
                        RequestPredicates.path("/api/v1/products"),
                        HandlerFunctions.http("http://localhost:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productsServiceRoutesApiDocs() {
        return GatewayRouterFunctions.route("product_service_swagger")
                .route(
                        RequestPredicates.path("/aggregate/product-service/v3/api-docs"),
                        HandlerFunctions.http("http://localhost:8080")).filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoutes() {
        return GatewayRouterFunctions.route("order_service")
                .route(
                        RequestPredicates.path("/api/v1/orders"),
                        HandlerFunctions.http("http://localhost:8081"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoutesApiDocs() {
        return GatewayRouterFunctions.route("order_service_swagger")
                .route(
                        RequestPredicates.path("/aggregate/order-service/v3/api-docs"),
                        HandlerFunctions.http("http://localhost:8081")).filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoriesServiceRoutes() {
        return GatewayRouterFunctions.route("inventory_service")
                .route(
                        RequestPredicates.path("/api/v1/inventories"),
                        HandlerFunctions.http("http://localhost:8082"))
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoutesApiDocs() {
        return GatewayRouterFunctions.route("inventory_service_swagger")
                .route(
                        RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"),
                        HandlerFunctions.http("http://localhost:8082")).filter(setPath("/api-docs"))
                .build();
    }
}
