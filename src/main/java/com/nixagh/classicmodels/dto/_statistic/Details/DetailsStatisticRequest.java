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
    private Date from;  // Từ ngày nào
    private Date to;    // Đến ngày nào
    private String typeProductLine; // Loại sản phẩm
    private String search;  // Từ khóa tìm kiếm theo tên sản phẩm hoặc mã sản phẩm
}
