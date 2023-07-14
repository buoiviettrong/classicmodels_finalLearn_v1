package com.nixagh.classicmodels.dto.orders.manager.history;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@Builder
public class OrderHistoryResponse {
    private Integer orderNumber;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date orderDate;
    private Double totalAmount;
    private String status;
}
