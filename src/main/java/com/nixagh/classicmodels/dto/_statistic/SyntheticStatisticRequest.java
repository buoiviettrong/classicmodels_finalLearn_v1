package com.nixagh.classicmodels.dto._statistic;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SyntheticStatisticRequest {
    private String from;
    private String to;
    private String type;
}
