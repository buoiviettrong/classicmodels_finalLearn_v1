package com.nixagh.classicmodels.dto.orders;

import com.nixagh.classicmodels.entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOrder {
  private String productCode;
  private Long quantity;
  private Double price;
}
