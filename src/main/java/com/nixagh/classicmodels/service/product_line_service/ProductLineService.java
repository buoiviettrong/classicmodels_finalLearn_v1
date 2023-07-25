package com.nixagh.classicmodels.service.product_line_service;

import com.nixagh.classicmodels.dto.product_line.ProductLineUpdateRequest;
import com.nixagh.classicmodels.entity.ProductLinee;
import com.nixagh.classicmodels.exception.exceptions.NotFoundEntity;
import com.nixagh.classicmodels.repository.product_line.ProductLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductLineService implements IProductLineService {

    private final ProductLineRepository productLineRepository;

    @Override
    public List<ProductLinee> getProductLines() {
        return productLineRepository.getProductLines();
    }

    @Override
    public ProductLinee getProductLine(String productLine) {
        return productLineRepository.getProductLine(productLine);
    }

    @Override
    public ProductLinee createProductLine(ProductLinee productLine) {
        return productLineRepository.save(productLine);
    }

    @Override
    public void updateProductLine(String productLine, ProductLineUpdateRequest request) {
        ProductLinee productLinee = getProductLine(productLine);

        if (productLinee == null) throw new NotFoundEntity("ProductLine not found");

        if (request.getTextDescription() != null) productLinee.setTextDescription(request.getTextDescription());
        if (request.getHtmlDescription() != null) productLinee.setHtmlDescription(request.getHtmlDescription());
        if (request.getImage() != null) productLinee.setImage(request.getImage());

        productLineRepository.save(productLinee);
    }

    @Override
    public void deleteProductLine(String productLine) {
        ProductLinee productLinee = getProductLine(productLine);

        if (productLinee == null) throw new NotFoundEntity("ProductLine not found");

        productLineRepository.deleteProductLinee(productLine);
    }

    @Override
    public List<String> getProductLinesSelect() {
        return productLineRepository.getProductLinesSelect();
    }
}
