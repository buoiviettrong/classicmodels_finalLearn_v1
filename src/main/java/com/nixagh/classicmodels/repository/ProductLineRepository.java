package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.ProductLinee;
import com.nixagh.classicmodels.repository.BaseRepository;

import java.util.List;

public interface ProductLineRepository extends BaseRepository<ProductLinee, String> {
    public List<ProductLinee> getProductLines();

    public ProductLinee getProductLine(String productLine);

    public void deleteProductLinee(String productLine);
}
