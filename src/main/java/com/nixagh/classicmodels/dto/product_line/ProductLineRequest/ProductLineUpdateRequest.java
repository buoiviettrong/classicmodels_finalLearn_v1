package com.nixagh.classicmodels.dto.product_line.ProductLineRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductLineUpdateRequest {
    private String textDescription;
    private String htmlDescription;
    private String image;
}
