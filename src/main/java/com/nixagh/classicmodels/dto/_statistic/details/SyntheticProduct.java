package com.nixagh.classicmodels.dto._statistic.details;

import com.nixagh.classicmodels.utils.math.RoundUtil;
import com.querydsl.core.Tuple;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyntheticProduct {
    private Long totalProduct = 0L;
    private List<SyntheticProductLine> syntheticProductLine = new ArrayList<>();

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SyntheticProductLine {
        private String productLineCode;
        private Long totalSoldProduct = 0L;
        private Double totalMoney = 0.0;

        public static SyntheticProductLine fromTuple(Tuple tuple) {
            var productLineCode = tuple.get(0, String.class);
            var totalSoldProduct = tuple.get(1, Long.class);
            var totalMoney = RoundUtil.convert(tuple.get(2, Double.class), 2);
            return SyntheticProductLine.builder()
                    .productLineCode(productLineCode)
                    .totalSoldProduct(totalSoldProduct)
                    .totalMoney(totalMoney)
                    .build();
        }
    }
}
