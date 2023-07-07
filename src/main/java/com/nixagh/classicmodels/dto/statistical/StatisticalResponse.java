package com.nixagh.classicmodels.dto.statistical;

import com.nixagh.classicmodels.dto.DateRange;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticalResponse {
    private DateRange timeRange;
    private OrderStatistic order;
    private Double totalProfit = (double) 0;
}
