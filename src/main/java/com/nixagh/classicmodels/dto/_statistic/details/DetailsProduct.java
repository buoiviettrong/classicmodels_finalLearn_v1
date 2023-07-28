package com.nixagh.classicmodels.dto._statistic.details;

import com.nixagh.classicmodels.utils.math.RoundUtil;
import jakarta.persistence.Tuple;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailsProduct {
    private String productCode; // Mã sản phẩm
    private String productName; // Tên sản phẩm
    private String productLine; // Loại sản phẩm
    private Long quantitySold;  // Số lượng đã bán
    private Double totalMoney;  // Tổng tiền

    public static DetailsProduct fromTuple(Tuple tuple) {
        var productCode = tuple.get("productCode", String.class);
        var productName = tuple.get("productName", String.class);
        var productLine = tuple.get("productLine", String.class);
        var quantitySold = tuple.get("totalSold", Long.class);
        var totalMoney = tuple.get("totalMoney", Double.class);

        return DetailsProduct.builder()
                .productCode(productCode)
                .productName(productName)
                .productLine(productLine)
                .quantitySold(quantitySold == null ? 0 : quantitySold)
                .totalMoney(totalMoney == null ? 0 : RoundUtil.convert(totalMoney, 2))
                .build();
    }

    public static DetailsProduct fromTuple(com.querydsl.core.Tuple tuple) {
        var productCode = tuple.get(0, String.class);
        var productName = tuple.get(1, String.class);
        var productLine = tuple.get(2, String.class);
        var quantitySold = tuple.get(3, Long.class);
        var totalMoney = tuple.get(4, Double.class);

        return DetailsProduct.builder()
                .productCode(productCode)
                .productName(productName)
                .productLine(productLine)
                .quantitySold(quantitySold == null ? 0 : quantitySold)
                .totalMoney(totalMoney == null ? 0 : RoundUtil.convert(totalMoney, 2))
                .build();
    }
}
