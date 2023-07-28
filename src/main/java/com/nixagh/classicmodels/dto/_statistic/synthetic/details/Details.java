package com.nixagh.classicmodels.dto._statistic.synthetic.details;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Details {
    private Date date;
    private Double totalMoney;
}
