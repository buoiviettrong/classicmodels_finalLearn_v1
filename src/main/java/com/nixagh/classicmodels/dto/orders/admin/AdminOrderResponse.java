package com.nixagh.classicmodels.dto.orders.admin;

import com.nixagh.classicmodels.dto.AbstractResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AdminOrderResponse extends AbstractResponse {
    private List<OrderResponse> orders = new ArrayList<>();

    public record OrderResponse(Long orderNumber, Date orderDate, Date shippedDate, String status, Long customersNumber,
                                String comments, Double totalAmount, String paymentStatus, Date paymentDate) {
    }
}
