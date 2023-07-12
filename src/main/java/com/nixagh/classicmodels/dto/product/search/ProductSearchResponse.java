package com.nixagh.classicmodels.dto.product.search;

import com.nixagh.classicmodels.dto.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchResponse extends AbstractResponse {
    List<ProductSearchResponseDTO> products = new ArrayList<>();
}
