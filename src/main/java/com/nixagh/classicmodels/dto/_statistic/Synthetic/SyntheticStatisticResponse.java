package com.nixagh.classicmodels.dto._statistic.Synthetic;

import com.nixagh.classicmodels.dto._statistic.Synthetic.details.SyntheticProduct;
import com.nixagh.classicmodels.dto._statistic.Synthetic.overview.Overview;
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


