package com.wales.microservice.order.controller;

import com.wales.microservice.order.dto.OrderRequest;
import com.wales.microservice.order.dto.OrderResponse;
import com.wales.microservice.order.model.Order;
import com.wales.microservice.order.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@RequestBody OrderRequest orderRequest) {
       return orderService.placeOrder(orderRequest);
    }
}
