package com.nixagh.classicmodels.dto._statistic;

import com.nixagh.classicmodels.dto._statistic.details.SyntheticProduct;
import com.nixagh.classicmodels.dto._statistic.overview.Overview;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyntheticStatisticResponse {
    private Overview overview;
    private SyntheticProduct syntheticProduct;
}


