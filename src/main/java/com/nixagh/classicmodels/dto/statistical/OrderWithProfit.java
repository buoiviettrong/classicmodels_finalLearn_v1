package com.nixagh.classicmodels.dto.statistical;

import com.nixagh.classicmodels.entity.Order;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderWithProfit {
    private Order order;
    private Double profit;
}
