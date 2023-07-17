package com.nixagh.classicmodels.dto.product.search;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductSearchFilter {
    private String productLine;
    private Integer productScale;
    private String productVendor;
    private QuantityInStock quantityInStock;
    private String search;
}

