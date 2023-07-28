package com.nixagh.classicmodels.dto._statistic.synthetic;

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
    private Date from;  // Ngày bắt đầu
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date to;    // Ngày kết thúc
}
