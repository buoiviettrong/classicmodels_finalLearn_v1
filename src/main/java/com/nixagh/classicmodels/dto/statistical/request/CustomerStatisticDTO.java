package com.nixagh.classicmodels.dto.statistical.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerStatisticDTO {
    private Long customerNumber;
    private String customerName;
    private Integer totalOrder;
    private Double totalAmount;
}
