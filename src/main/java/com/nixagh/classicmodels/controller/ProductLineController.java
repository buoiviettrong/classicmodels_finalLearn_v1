package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels.dto.product_line.ProductLineRequest.ProductLineUpdateRequest;
import com.nixagh.classicmodels.entity.ProductLinee;
import com.nixagh.classicmodels.service.ProductLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-line")
@RequiredArgsConstructor
@EnableCaching
@PreAuthorize("hasRole('ADMIN')")
public class ProductLineController {
    public final ProductLineService productLineService;

    @GetMapping
    @Cacheable(value = "productLines", cacheManager = "cacheManager")
    public List<ProductLinee> getProductLines() {
        return productLineService.getProductLines();
    }

    @GetMapping("/{productLine}")
    @Cacheable(value = "productLine", key = "#productLine", cacheManager = "cacheManager")
    public ProductLinee getProductLine(@PathVariable String productLine) {
        return productLineService.getProductLine(productLine);
    }

    @PostMapping()
    public ProductLinee createProductLine(@RequestBody ProductLinee productLine) {
        return productLineService.createProductLine(productLine);
    }

    @PutMapping("/{productLine}")
    public ResponseEntity<?> updateProductLine(@PathVariable String productLine, @RequestBody ProductLineUpdateRequest request) {
        productLineService.updateProductLine(productLine, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{productLine}")
    public ResponseEntity<?> deleteProductLine(@PathVariable String productLine) {
        productLineService.deleteProductLine(productLine);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
