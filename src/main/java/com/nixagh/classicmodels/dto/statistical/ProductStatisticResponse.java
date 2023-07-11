package com.nixagh.classicmodels.dto.statistical;

import com.nixagh.classicmodels.dto.AbstractResponse;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductStatisticResponse extends AbstractResponse {
    private List<ProductStatisticDTO> products = new ArrayList<ProductStatisticDTO>();
}
