package com.nixagh.classicmodels.repository.product_line;

import com.nixagh.classicmodels.entity.ProductLinee;
import com.nixagh.classicmodels.repository.BaseRepository;

import java.util.List;

public interface ProductLineRepository extends BaseRepository<ProductLinee, String> {
    List<ProductLinee> getProductLines();

    ProductLinee getProductLine(String productLine);

    void deleteProductLinee(String productLine);

    List<String> getProductLinesSelect();
}
