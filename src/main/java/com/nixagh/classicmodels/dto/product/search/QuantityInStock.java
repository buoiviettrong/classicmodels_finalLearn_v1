package com.nixagh.classicmodels.dto.product.search;

public record QuantityInStock(Long min, Long max) {
    private static final Long DEFAULT_MIN = 0L;
    private static final Long DEFAULT_MAX = 0L;
}
