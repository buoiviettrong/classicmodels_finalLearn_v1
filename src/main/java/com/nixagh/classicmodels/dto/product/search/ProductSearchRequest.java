package com.nixagh.classicmodels.dto.product.search;

import com.nixagh.classicmodels.dto.AbstractRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductSearchRequest extends AbstractRequest {
    private ProductSearchFilter filter = new ProductSearchFilter();

}
