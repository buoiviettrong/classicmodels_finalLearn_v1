package com.nixagh.classicmodels.dto.orders.admin.statictis.customer.orders.detail;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrderDetailResponse {
    private Integer orderNumber;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date orderDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date shippedDate;
    private String status;
    private String comments;
    private Double totalPrice;
}
