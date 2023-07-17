package com.nixagh.classicmodels.dto.orders.admin.statictis.order;

import com.nixagh.classicmodels.dto.AbstractResponse;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse extends AbstractResponse {
    private List<OrderDetails> orders = new ArrayList<>();
    private Map<String, Integer> status;
}

