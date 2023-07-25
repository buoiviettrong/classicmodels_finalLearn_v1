package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels.dto.product.ProductAddRequest;
import com.nixagh.classicmodels.dto.product.edit.ProductUpdateRequest;
import com.nixagh.classicmodels.dto.product.manager.search.request.ProductManagerSearchRequest;
import com.nixagh.classicmodels.dto.product.search.ProductSearchRequest;
import com.nixagh.classicmodels.dto.product.search.ProductSearchResponse;
import com.nixagh.classicmodels.dto.product.search.ProductSearchResponseDTO;
import com.nixagh.classicmodels.entity.Product;
import com.nixagh.classicmodels.service.product_service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final IProductService productService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> addProduct(@RequestBody ProductAddRequest product) {
        return productService.addProduct(product);
    }

    @PostMapping("/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductSearchResponse filterProducts(@RequestBody ProductSearchRequest request) {
        System.out.println(request);
        return productService.filterProducts(request);
    }

    @DeleteMapping("/{productCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> deleteProduct(@PathVariable String productCode) {
        return productService.deleteProduct(productCode);
    }

    @GetMapping("/{productCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ProductSearchResponseDTO getProduct(@PathVariable String productCode) {
        return productService.getProduct(productCode);
    }

    @PutMapping("/{productCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> updateProduct(@PathVariable String productCode, @RequestBody ProductUpdateRequest product) {
        return productService.updateProduct(productCode, product);
    }

    @PostMapping("/manager-search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ProductSearchResponse managerSearch(@RequestBody ProductManagerSearchRequest request) {
        return productService.managerSearch(request);
    }

    @GetMapping("/out-of-stock")
    public List<ProductOutOfStockResponse> getOutOfStockProducts() {
        return productService.getOutOfStockProducts();
    }

    @PutMapping("/update-quantity-in-stock/{productCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> updateQuantityInStock(
            @PathVariable(name = "productCode") String productCode,
            @RequestBody Map<String, Integer> request
    ) {
        var quantityInStock = request.get("quantityInStock");
        return productService.updateQuantityInStock(productCode, quantityInStock);
    }

    public record ProductOutOfStockResponse(String productCode, String productName, Integer quantityInStock) {
    }
}
