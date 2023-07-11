package com.nixagh.classicmodels.dto.statistical.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderStatusStatisticResponse {
    private String status;
    private Long count;
    private Double amount;
}
