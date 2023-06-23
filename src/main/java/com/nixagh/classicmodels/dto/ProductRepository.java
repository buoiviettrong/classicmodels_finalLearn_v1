package com.nixagh.classicmodels.dto;

import com.nixagh.classicmodels.entity.Product;
import com.nixagh.classicmodels.repository.BaseRepository;
import org.springframework.stereotype.Repository;

public interface ProductRepository extends BaseRepository<Product, String> {
  public Product findProductByProductCode(String productCode);
}
