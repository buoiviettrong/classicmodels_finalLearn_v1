package com.nixagh.classicmodels.dto._statistic.overview;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OverviewTotal {
    private Long totalInvoice;
    private Double totalMoney;
    private Long totalSoldProduct;
}
