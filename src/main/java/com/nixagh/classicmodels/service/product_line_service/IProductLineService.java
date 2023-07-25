package com.nixagh.classicmodels.service.product_line_service;

import com.nixagh.classicmodels.dto.product_line.ProductLineUpdateRequest;
import com.nixagh.classicmodels.entity.ProductLinee;

import java.util.List;

public interface IProductLineService {
    List<ProductLinee> getProductLines();

    ProductLinee getProductLine(String productLine);

    ProductLinee createProductLine(ProductLinee productLine);

    void updateProductLine(String productLine, ProductLineUpdateRequest request);

    void deleteProductLine(String productLine);

    List<String> getProductLinesSelect();
}
