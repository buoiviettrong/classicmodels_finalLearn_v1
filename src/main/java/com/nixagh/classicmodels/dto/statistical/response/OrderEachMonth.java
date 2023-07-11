package com.nixagh.classicmodels.dto.statistical.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEachMonth {
    private Integer month;
    private Long totalOrder;
    private Long successOrder;
    private Long cancellerOrder;
    private Long otherOrder;
    private Double successProfit;
    private Double cancellerProfit;
    private Double otherProfit;
    private Double totalProfit;
}
