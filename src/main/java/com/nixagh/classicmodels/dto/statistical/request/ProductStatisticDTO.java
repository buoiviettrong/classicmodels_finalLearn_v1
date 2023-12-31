package com.nixagh.classicmodels.dto.statistical.request;

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
    private Double buyPrice;
    private Double soldPrice;
    private Double totalProfit = 0.0;
}
