package com.nixagh.classicmodels.dto.statistical;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Top10ProductResponse {
    private String productCode;
    private Long totalSoldQuantity;
    private Double totalProfit;
}
