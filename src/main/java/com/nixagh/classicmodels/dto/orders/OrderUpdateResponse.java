package com.nixagh.classicmodels.dto.orders;

import com.nixagh.classicmodels.dto.Message.Message;
import com.nixagh.classicmodels.entity.Order;
import lombok.*;
import org.aspectj.weaver.ast.Or;

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
