package com.nixagh.classicmodels.dto.orders;

import com.nixagh.classicmodels.dto.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderDeleteResponse {
    private List<Message> messages = new ArrayList<>();
    private Long orderId;
}
