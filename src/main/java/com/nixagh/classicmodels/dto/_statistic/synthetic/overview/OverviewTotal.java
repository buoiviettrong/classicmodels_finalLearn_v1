package com.nixagh.classicmodels.dto._statistic.synthetic.overview;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OverviewTotal {
    private Long totalInvoice = 0L;
    private Double totalMoney = 0.0;
    private Long totalSoldProduct = 0L;
}
