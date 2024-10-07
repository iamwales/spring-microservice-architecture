package com.wales.microservice.order.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface InventoryClientWithRestClient {

    @GetExchange("/api/v1/inventories")
    boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);
}
