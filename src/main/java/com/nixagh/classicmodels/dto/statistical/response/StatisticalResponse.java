package com.nixagh.classicmodels.dto.statistical.response;

import com.nixagh.classicmodels.dto.date.DateRange;
import com.nixagh.classicmodels.dto.statistical.request.OrderStatistic;
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
