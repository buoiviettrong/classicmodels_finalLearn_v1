package com.nixagh.classicmodels.dto.statistical.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatisticDTO {
    private Long orderNumber;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date orderDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date shippedDate;
    private String status;
    private Long customerNumber;
    private String comment;
}
