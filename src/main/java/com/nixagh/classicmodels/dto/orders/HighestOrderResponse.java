package com.nixagh.classicmodels.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class HighestOrderResponse {
    private Long orderId;
    private Double totalPrice;
}
