package com.wales.microservice.product.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponse(String id, String name, String description, BigDecimal price) {
}
