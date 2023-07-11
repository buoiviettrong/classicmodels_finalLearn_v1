package com.nixagh.classicmodels.dto.statistical.response;

import com.nixagh.classicmodels.dto.AbstractResponse;
import com.nixagh.classicmodels.dto.statistical.request.CustomerStatisticDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerEachMonth extends AbstractResponse {
    List<CustomerStatisticDTO> customers = new ArrayList<>();
}
