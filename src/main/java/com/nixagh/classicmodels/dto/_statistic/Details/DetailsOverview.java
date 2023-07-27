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
    private String productLineCode; // Mã loại sản phẩm
    private Long totalSold; // Tổng số lượng bán được
    private Double totalMoney; // Tổng tiền bán được

    // Convert from Tuple to DetailsOverview
    public static DetailsOverview fromTuple(Tuple tuple) {
        if (tuple == null) return DetailsOverview.builder()
                .productLineCode("")
                .totalSold(0L)
                .totalMoney(0D)
                .build();

        var productLineCode = tuple.get("productLineCode", String.class);
        var totalSold = tuple.get("totalSold", Long.class);
        var totalMoney = tuple.get("totalMoney", Double.class);

        return DetailsOverview.builder()
                .productLineCode(productLineCode)
                .totalSold(totalSold == null ? 0L : totalSold)
                .totalMoney(totalMoney == null ? 0D : RoundUtil.convert(totalMoney, 2))
                .build();
    }

    // Convert from Tuple to DetailsOverview
    public static DetailsOverview fromTuple(com.querydsl.core.Tuple tuple) {
        if (tuple == null) return DetailsOverview.builder()
                .productLineCode("")
                .totalSold(0L)
                .totalMoney(0D)
                .build();

        var productLineCode = tuple.get(0, String.class);
        var totalSold = tuple.get(1, Long.class);
        var totalMoney = tuple.get(2, Double.class);

        return DetailsOverview.builder()
                .productLineCode(productLineCode)
                .totalSold(totalSold == null ? 0L : totalSold)
                .totalMoney(totalMoney == null ? 0D : RoundUtil.convert(totalMoney, 2))
                .build();
    }
}
