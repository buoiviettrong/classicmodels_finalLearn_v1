package com.nixagh.classicmodels.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductAddRequest {
    private Integer productCode;
    private String productName;
    private String productLine;
    private Integer productScale;
    private String productVendor;
    private String productDescription;
    private Double buyPrice;
    private Double msrp;
}
