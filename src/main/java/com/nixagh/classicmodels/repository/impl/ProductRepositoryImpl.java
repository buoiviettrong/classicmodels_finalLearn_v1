package com.nixagh.classicmodels.repository.impl;

import com.nixagh.classicmodels.dto.ProductRepository;
import com.nixagh.classicmodels.entity.Product;
import jakarta.persistence.EntityManager;

public class ProductRepositoryImpl extends BaseRepositoryImpl<Product, String> implements ProductRepository {
  public ProductRepositoryImpl(EntityManager entityManager) {
    super(Product.class, entityManager);
  }

  @Override
  public Product findProductByProductCode(String productCode) {
    return jpaQueryFactory
        .selectFrom(product)
        .where(product.productCode.eq(productCode))
        .fetchOne();
  }
}
