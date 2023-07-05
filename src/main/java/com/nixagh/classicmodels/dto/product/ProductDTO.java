package com.nixagh.classicmodels.dto.product;

import java.io.Serializable;

public record ProductDTO(
        String productCode,
        String productName,
        String productDescription,
        String productVendor,
        Long quantityOrdered,
        Double priceEach
) implements Serializable {
}
