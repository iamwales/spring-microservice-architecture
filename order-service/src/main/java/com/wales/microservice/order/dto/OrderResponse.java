package com.wales.microservice.order.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderResponse(Long id, String orderNumber, String skuCode, BigDecimal price, Integer quantity) {
}
