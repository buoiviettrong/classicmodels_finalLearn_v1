package com.nixagh.classicmodels.dto.orders;

import com.nixagh.classicmodels.dto.message.Message;
import com.nixagh.classicmodels.entity.Order;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateResponse {
    private List<Message> messages = new ArrayList<>();
    private Order order;
}
