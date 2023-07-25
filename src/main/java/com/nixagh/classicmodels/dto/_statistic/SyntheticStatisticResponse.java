package com.nixagh.classicmodels.dto._statistic;

import com.nixagh.classicmodels.dto._statistic.details.Details;
import com.nixagh.classicmodels.dto._statistic.overview.Overview;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyntheticStatisticResponse {
    private Overview overview;
    private List<Details> details;
}


