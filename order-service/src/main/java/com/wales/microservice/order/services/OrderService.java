package com.wales.microservice.order.services;

import com.wales.microservice.order.client.InventoryClientWithOpenFeign;
import com.wales.microservice.order.client.InventoryClientWithRestClient;
import com.wales.microservice.order.dto.OrderRequest;
import com.wales.microservice.order.dto.OrderResponse;
import com.wales.microservice.order.events.OrderPlacedEvent;
import com.wales.microservice.order.model.Order;
import com.wales.microservice.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClientWithOpenFeign inventoryClientWithOpenFeign;
    private final InventoryClientWithRestClient inventoryClientWithRestClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public OrderResponse placeOrder(OrderRequest orderRequest) {

        System.out.println("I got here " + orderRequest);

        var isOderInStock = inventoryClientWithRestClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (isOderInStock) {
            System.out.println("order is in stock");
            Order order = Order.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .price(orderRequest.price())
                    .skuCode(orderRequest.skuCode())
                    .quantity(orderRequest.quantity())
                    .build();

            var createdOrder = orderRepository.save(order);

            // Send the message to Kafka topic
            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
            orderPlacedEvent.setOrderNumber(order.getOrderNumber());
            orderPlacedEvent.setEmail(orderRequest.userDetails().email());
            orderPlacedEvent.setFirstName(orderRequest.userDetails().firstName());
            orderPlacedEvent.setLastName(orderRequest.userDetails().lastName());

            log.info("Start - Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);
            kafkaTemplate.send("order-placed", orderPlacedEvent);
            log.info("End - Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);


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
