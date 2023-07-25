package com.nixagh.classicmodels.dto.statistical;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public abstract class StatisticAbstractResponse<T> implements Serializable {
    @Setter
    @Getter
    static class Group {
        private int totalOrder;
        private Double totalAmount;
        private Double totalProfit;
    }

    private Group group;
    private T data;
}
