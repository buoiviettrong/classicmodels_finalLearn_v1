package com.nixagh.classicmodels.dto.product;

public record ProductDTO(
        String productCode,
        String productName,
        String productDescription,
        String productVendor,
        Long quantityOrdered,
        Double priceEach
) {
}
