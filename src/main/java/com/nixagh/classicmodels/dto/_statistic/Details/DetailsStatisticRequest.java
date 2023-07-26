package com.nixagh.classicmodels.dto._statistic.Details;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DetailsStatisticRequest {
    private Date from;
    private Date to;
    private String typeProductLine;
    private String search;
}
