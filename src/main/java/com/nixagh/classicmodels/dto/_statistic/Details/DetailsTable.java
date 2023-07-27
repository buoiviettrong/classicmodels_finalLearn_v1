package com.nixagh.classicmodels.dto._statistic.Details;

import com.nixagh.classicmodels.dto.AbstractResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailsTable extends AbstractResponse {
    private List<DetailsProduct> products; // Danh sách sản phẩm
    private Long totalQuantity; // Tổng số lượng
    private Double totalMoney; // Tổng tiền
}
