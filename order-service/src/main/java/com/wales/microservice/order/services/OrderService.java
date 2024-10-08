package com.wales.microservice.order.services;

import com.wales.microservice.order.client.InventoryClientWithOpenFeign;
import com.wales.microservice.order.client.InventoryClientWithRestClient;
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
    private final InventoryClientWithOpenFeign inventoryClientWithOpenFeign;
    private final InventoryClientWithRestClient inventoryClientWithRestClient;

    public OrderResponse placeOrder(OrderRequest orderRequest) {

        var isOderInStock = inventoryClientWithRestClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (isOderInStock) {
            Order order = Order.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .price(orderRequest.price())
                    .skuCode(orderRequest.skuCode())
                    .quantity(orderRequest.quantity())
                    .build();

            var createdOrder = orderRepository.save(order);

            return OrderResponse.builder()
                    .id(createdOrder.getId())
                    .orderNumber(createdOrder.getOrderNumber())
                    .price(createdOrder.getPrice())
                    .skuCode(createdOrder.getSkuCode())
                    .quantity(createdOrder.getQuantity())
                    .build();
        } else {
            throw new RuntimeException("Product with SkuCode " + orderRequest.skuCode() + "is not in stock");
        }


    }
}
