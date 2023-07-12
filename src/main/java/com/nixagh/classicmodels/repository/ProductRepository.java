package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.dto.product.search.ProductSearchResponseDTO;
import com.nixagh.classicmodels.dto.product.search.QuantityInStock;
import com.nixagh.classicmodels.entity.Product;
import com.querydsl.core.Tuple;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends BaseRepository<Product, String> {
    Product findProductByProductCode(String productCode);

    List<Tuple> findProductsEachMonthInYear_(int year);

    List<Tuple> findAllStatistic();

    Optional<Product> findByProductCode(String productCode);

    List<ProductSearchResponseDTO> filterProducts(
//            String productCode,
//            String productName,
            String productLine,
            Integer productScale,
            String productVendor,
//            String productDescription,
            QuantityInStock quantityInStock,
            Long offset, Long pageSize
    );

    Long countFilterProducts(
//            String productCode,
//            String productName,
            String productLine,
            Integer productScale,
            String productVendor,
//            String productDescription,
            QuantityInStock quantityInStock
    );

    ProductSearchResponseDTO getProduct(String productCode);
}
