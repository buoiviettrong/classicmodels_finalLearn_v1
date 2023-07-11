package com.nixagh.classicmodels.dto.statistical;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatisticDTO {
    private Long orderNumber;
    private Date orderDate;
    private Date shippedDate;
    private String status;
    private Long customerNumber;
    private String comment;
}
