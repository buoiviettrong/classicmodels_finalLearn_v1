package com.nixagh.classicmodels.dto._statistic.Details;

import com.nixagh.classicmodels.utils.math.RoundUtil;
import jakarta.persistence.Tuple;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DetailsOverview {
    private String productLineCode;
    private Long totalSold;
    private Double totalMoney;

    public static DetailsOverview fromTuple(Tuple tuple) {
        if (tuple == null) return DetailsOverview.builder()
                .productLineCode("")
                .totalSold(0L)
                .totalMoney(0D)
                .build();

        return DetailsOverview.builder()
                .productLineCode(tuple.get("productLineCode", String.class) == null ? "No Item" : tuple.get("productLineCode", String.class))
                .totalSold(tuple.get("totalSold", Long.class) == null ? 0L : tuple.get("totalSold", Long.class))
                .totalMoney(tuple.get("totalMoney", Double.class) == null ? 0D : RoundUtil.convert(tuple.get("totalMoney", Double.class), 2))
                .build();
    }

    public static DetailsOverview fromTuple(com.querydsl.core.Tuple tuple) {
        if (tuple == null) return DetailsOverview.builder()
                .productLineCode("")
                .totalSold(0L)
                .totalMoney(0D)
                .build();

        return DetailsOverview.builder()
                .productLineCode(tuple.get(0, String.class) == null ? "No Item" : tuple.get(0, String.class))
                .totalSold(tuple.get(1, Long.class) == null ? 0L : tuple.get(1, Long.class))
                .totalMoney(tuple.get(2, Double.class) == null ? 0D : RoundUtil.convert(tuple.get(2, Double.class), 2))
                .build();
    }
}
