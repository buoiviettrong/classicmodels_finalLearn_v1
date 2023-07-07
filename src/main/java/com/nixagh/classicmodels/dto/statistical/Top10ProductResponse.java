package com.nixagh.classicmodels.dto.statistical;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Top10ProductResponse {
    private String productCode;
    private String productName;
    private Long totalSoldQuantity;
    private Double totalProfit;
}
