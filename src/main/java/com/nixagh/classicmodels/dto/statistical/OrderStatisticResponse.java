package com.nixagh.classicmodels.dto.statistical;

import com.nixagh.classicmodels.dto.AbstractResponse;
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
