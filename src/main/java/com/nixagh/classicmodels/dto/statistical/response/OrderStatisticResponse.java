package com.nixagh.classicmodels.dto.statistical.response;

import com.nixagh.classicmodels.dto.AbstractResponse;
import com.nixagh.classicmodels.dto.statistical.request.OrderStatisticDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatisticResponse extends AbstractResponse {
    List<OrderStatisticDTO> orders = new ArrayList<OrderStatisticDTO>();
}
