package com.wales.microservice.product.dto;

import java.math.BigDecimal;

public record ProductRequest(String id, String name, String description, String skuCode, BigDecimal price) {
}
