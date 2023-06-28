package com.nixagh.classicmodels.dto.orders;

import com.nixagh.classicmodels.dto.AbstractMessageResponse;
import com.nixagh.classicmodels.entity.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderGetResponse extends AbstractMessageResponse {
    private Order order;
}
