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
public class CustomerStatisticResponse extends AbstractResponse {
    private List<CustomerStatisticDTO> customers = new ArrayList<>();
}
