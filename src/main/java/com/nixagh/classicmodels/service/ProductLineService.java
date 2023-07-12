package com.nixagh.classicmodels.service;

import com.nixagh.classicmodels.dto.product_line.ProductLineUpdateRequest;
import com.nixagh.classicmodels.entity.ProductLinee;
import com.nixagh.classicmodels.exception.NotFoundEntity;
import com.nixagh.classicmodels.repository.ProductLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductLineService {

    private final ProductLineRepository productLineRepository;

    public List<ProductLinee> getProductLines() {
        return productLineRepository.getProductLines();
    }

    public ProductLinee getProductLine(String productLine) {
        return productLineRepository.getProductLine(productLine);
    }

    public ProductLinee createProductLine(ProductLinee productLine) {
        return productLineRepository.save(productLine);
    }

    public void updateProductLine(String productLine, ProductLineUpdateRequest request) {
        ProductLinee productLinee = getProductLine(productLine);

        if (productLinee == null) throw new NotFoundEntity("ProductLine not found");

        if (request.getTextDescription() != null) productLinee.setTextDescription(request.getTextDescription());
        if (request.getHtmlDescription() != null) productLinee.setHtmlDescription(request.getHtmlDescription());
        if (request.getImage() != null) productLinee.setImage(request.getImage());

        productLineRepository.save(productLinee);
    }

    public void deleteProductLine(String productLine) {
        ProductLinee productLinee = getProductLine(productLine);

        if (productLinee == null) throw new NotFoundEntity("ProductLine not found");

        productLineRepository.deleteProductLinee(productLine);
    }

    public List<String> getProductLinesSelect() {
        return productLineRepository.getProductLinesSelect();
    }
}
