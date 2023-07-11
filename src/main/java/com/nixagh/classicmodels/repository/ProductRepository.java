package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.Product;
import com.querydsl.core.Tuple;

import java.util.List;

public interface ProductRepository extends BaseRepository<Product, String> {
    Product findProductByProductCode(String productCode);

    List<Tuple> findProductsEachMonthInYear_(int year);

    List<Tuple> findAllStatistic();
}
