package com.nixagh.classicmodels.dto.statistical;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductStatisticDTO {
    private String productCode;
    private String productName;
    private Long totalSoldQuantity;
    private Double totalAmount;
}