package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.ProductLinee;

import java.util.List;

public interface ProductLineRepository extends BaseRepository<ProductLinee, String> {
    List<ProductLinee> getProductLines();

    ProductLinee getProductLine(String productLine);

    void deleteProductLinee(String productLine);

    List<String> getProductLinesSelect();
}
