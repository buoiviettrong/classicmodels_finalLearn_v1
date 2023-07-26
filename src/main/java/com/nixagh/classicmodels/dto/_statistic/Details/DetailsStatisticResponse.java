package com.nixagh.classicmodels.dto._statistic.Details;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailsStatisticResponse {
    private DetailsOverview overview;
    private DetailsTable table;
}
