package com.wales.microservice.order.services;

import com.wales.microservice.order.dto.OrderRequest;
import com.wales.microservice.order.dto.OrderResponse;
import com.wales.microservice.order.model.Order;
import com.wales.microservice.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        System.out.println("orderRequest " + orderRequest);
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .price(orderRequest.price())
                .skuCode(orderRequest.skuCode())
                .quantity(orderRequest.quantity())
                .build();

        System.out.println("order " + order);

        var createdOrder = orderRepository.save(order);

        System.out.println("createdOrder "+ createdOrder);

        return OrderResponse.builder()
                .id(createdOrder.getId())
                .orderNumber(createdOrder.getOrderNumber())
                .price(createdOrder.getPrice())
                .skuCode(createdOrder.getSkuCode())
                .quantity(createdOrder.getQuantity())
                .build();
    }
}
