package com.nixagh.classicmodels.dto.statistical.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderEachMonthDTO {
    private String year;
    private String month;
    private String status;
}
