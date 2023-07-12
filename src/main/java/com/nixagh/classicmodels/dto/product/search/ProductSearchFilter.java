package com.nixagh.classicmodels.dto.product.search;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductSearchFilter {
    //    private String productCode;
//    private String productName;
    private String productLine;
    private Integer productScale;
    private String productVendor;
    //    private String productDescription;
    private QuantityInStock quantityInStock;
}

