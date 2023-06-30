package com.nixagh.classicmodels.dto.orders;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOrder {
    private String productCode;
    private Long quantity;
    private Double price;
}
