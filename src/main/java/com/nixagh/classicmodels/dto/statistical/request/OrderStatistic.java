package com.nixagh.classicmodels.dto.statistical.request;

import com.nixagh.classicmodels.dto.AbstractResponse;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatistic extends AbstractResponse implements Serializable {
    private Long totalOrder;
    private List<OrderWithProfit> orders;
}
