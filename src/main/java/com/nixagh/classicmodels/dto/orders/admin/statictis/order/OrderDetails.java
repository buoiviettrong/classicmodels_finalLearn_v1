package com.nixagh.classicmodels.dto.orders.admin.statictis.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class OrderDetails {
    private Long orderNumber;
    private Long customerNumber;
    private String customerName;
    private Date orderDate;
    private Date requiredDate;
    private Date shippedDate;
    private String status;
    private String comments;
    private Double totalPrice;
}
