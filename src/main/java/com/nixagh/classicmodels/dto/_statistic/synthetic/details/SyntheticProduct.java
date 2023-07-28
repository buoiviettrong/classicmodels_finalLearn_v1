package com.nixagh.classicmodels.dto._statistic.synthetic.details;

import com.nixagh.classicmodels.utils.math.RoundUtil;
import jakarta.persistence.Tuple;
import lombok.*;

import java.math.BigDecimal;
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
            var productLineCode = tuple.get("productLineCode", String.class);
            var totalSoldProduct = tuple.get("totalSoldProduct", BigDecimal.class);
            var totalMoney = tuple.get("totalMoney", Double.class);

            return SyntheticProductLine.builder()
                    .productLineCode(productLineCode == null ? "" : productLineCode)
                    .totalSoldProduct(totalSoldProduct == null ? 0L : totalSoldProduct.longValue())
                    .totalMoney(totalMoney == null ? 0.0 : RoundUtil.convert(totalMoney, 2))
                    .build();
        }
    }
}
