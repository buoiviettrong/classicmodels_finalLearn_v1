package com.nixagh.classicmodels.dto.product.edit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateRequest {
    private String productCode;
    private String productName;
    private String productLine;
    private Integer productScale;
    private String productVendor;
    private String productDescription;
    private Integer quantityInStock;
    private Double buyPrice;
    private Double msrp;
}
