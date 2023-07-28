package com.nixagh.classicmodels.dto._statistic.details;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailsStatisticResponse {
    private DetailsOverview overview; // Tổng quan
    private DetailsTable table; // Bảng chi tiết
}
