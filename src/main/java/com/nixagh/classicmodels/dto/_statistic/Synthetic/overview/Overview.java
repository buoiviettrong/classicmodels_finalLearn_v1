package com.nixagh.classicmodels.dto._statistic.Synthetic.overview;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Overview {
    private OverviewTotal overviewTotal;
    private OverviewTop overviewTop;
}
