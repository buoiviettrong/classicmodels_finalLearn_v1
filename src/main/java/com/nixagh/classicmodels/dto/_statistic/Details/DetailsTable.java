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
    private List<DetailsProduct> products;
    private Long totalQuantity;
    private Double totalMoney;
}
