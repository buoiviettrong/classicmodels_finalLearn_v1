package com.nixagh.classicmodels.dto.statistical.response;

import com.nixagh.classicmodels.dto.AbstractResponse;
import com.nixagh.classicmodels.dto.statistical.request.CustomerStatisticDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerStatisticResponse extends AbstractResponse {
    private List<CustomerStatisticDTO> customers = new ArrayList<>();
}
