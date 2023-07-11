package com.nixagh.classicmodels.dto.statistical.response;

import com.nixagh.classicmodels.dto.AbstractResponse;
import com.nixagh.classicmodels.dto.statistical.request.ProductStatisticDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEachMonth extends AbstractResponse {
    List<ProductStatisticDTO> products;
}
