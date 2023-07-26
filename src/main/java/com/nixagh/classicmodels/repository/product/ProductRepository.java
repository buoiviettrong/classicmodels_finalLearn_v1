package com.nixagh.classicmodels.repository.product;

import com.nixagh.classicmodels.controller.ProductController;
import com.nixagh.classicmodels.dto.product.search.ProductSearchResponseDTO;
import com.nixagh.classicmodels.dto.product.search.QuantityInStock;
import com.nixagh.classicmodels.entity.Product;
import com.nixagh.classicmodels.repository.BaseRepository;
import com.querydsl.core.Tuple;

import java.util.Date;
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

    Tuple getTotalSoldProductAndProfit(Date from, Date to);

    Tuple getTop1Product(Date from, Date to);

    Tuple getTop1ProductLine(Date from, Date to);

    Long getTotalProduct(Date from, Date to);

    List<Tuple> getSyntheticProductLineDSL(Date from, Date to);

    Tuple getTotalSoldProductAndProfit(Date from, Date to, String typeProductLine, String search);

    List<Tuple> getDetailStatisticDetail(Date from, Date to, String typeProductLine, String search, long offset, long pageSize);

    Long countDetailStatisticDetail(java.sql.Date sqlFrom, java.sql.Date sqlTo, String typeProductLine, String search);
}
