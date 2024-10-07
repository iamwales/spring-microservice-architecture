package com.wales.microservice.order.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface InventoryClientWithRestClient {

    Logger log = LoggerFactory.getLogger(InventoryClientWithRestClient.class);

    @GetExchange("/api/v1/inventories")
    // the name inventory should match with what was provided in the application.properties file
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @Retry(name = "inventory")
    boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);

    default boolean fallbackMethod(String code, Integer quantity, Throwable throwable) {
        log.info("Cannot get inventory for skuCode {}, failure reason {}", code, throwable.getMessage());
        return false;
    }
}
