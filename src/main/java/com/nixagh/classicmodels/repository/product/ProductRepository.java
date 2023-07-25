package com.nixagh.classicmodels.repository.product;

import com.nixagh.classicmodels.controller.ProductController;
import com.nixagh.classicmodels.dto._statistic.overview.OverviewTop;
import com.nixagh.classicmodels.dto.product.search.ProductSearchResponseDTO;
import com.nixagh.classicmodels.dto.product.search.QuantityInStock;
import com.nixagh.classicmodels.entity.Product;
import com.nixagh.classicmodels.repository.BaseRepository;
import com.querydsl.core.Tuple;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends BaseRepository<Product, String> {
    Product findProductByProductCode(String productCode);

    List<Tuple> findProductsEachMonthInYear_(int year);

    List<Tuple> findAllStatistic();

    Optional<Product> findByProductCode(String productCode);

    List<ProductSearchResponseDTO> filterProducts(
            String search,
            String productLine,
            Integer productScale,
            String productVendor,
            QuantityInStock quantityInStock,
            Long offset, Long pageSize
    );

    Long countFilterProducts(
            String search,
            String productLine,
            Integer productScale,
            String productVendor,
            QuantityInStock quantityInStock
    );

    ProductSearchResponseDTO getProduct(String productCode);

    List<ProductSearchResponseDTO> managerSearch(String search, String productLine, Long offset, Long pageSize);

    Long countManagerSearch(String search, String productLine);

    List<ProductController.ProductOutOfStockResponse> getOutOfStockProducts();

    Tuple getTotalSoldProductAndProfit(String from, String to);

    OverviewTop.Product getTop1Product(String from, String to);

    OverviewTop.ProductLine getTop1ProductLine(String from, String to);
}
