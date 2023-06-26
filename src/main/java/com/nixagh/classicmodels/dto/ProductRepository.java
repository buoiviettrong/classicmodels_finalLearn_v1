package com.nixagh.classicmodels.dto;

import com.nixagh.classicmodels.entity.Product;
import com.nixagh.classicmodels.repository.BaseRepository;

public interface ProductRepository extends BaseRepository<Product, String> {
  Product findProductByProductCode(String productCode);
}
