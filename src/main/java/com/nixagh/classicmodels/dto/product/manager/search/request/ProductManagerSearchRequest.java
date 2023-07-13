package com.nixagh.classicmodels.dto.product.manager.search.request;

import com.nixagh.classicmodels.dto.AbstractRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductManagerSearchRequest extends AbstractRequest {
    private String search;
}
