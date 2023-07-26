package com.nixagh.classicmodels.dto._statistic.Synthetic;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SyntheticStatisticRequest {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date from;
    private Date to;
    private String type;
}