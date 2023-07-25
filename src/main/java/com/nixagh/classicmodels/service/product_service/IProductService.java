package com.nixagh.classicmodels.service.product_service;

import com.nixagh.classicmodels.controller.ProductController;
import com.nixagh.classicmodels.controller.StatisticalController;
import com.nixagh.classicmodels.dto._statistic.overview.OverviewTop;
import com.nixagh.classicmodels.dto._statistic.overview.OverviewTotal;
import com.nixagh.classicmodels.dto.product.ProductAddRequest;
import com.nixagh.classicmodels.dto.product.edit.ProductUpdateRequest;
import com.nixagh.classicmodels.dto.product.manager.search.request.ProductManagerSearchRequest;
import com.nixagh.classicmodels.dto.product.search.ProductSearchRequest;
import com.nixagh.classicmodels.dto.product.search.ProductSearchResponse;
import com.nixagh.classicmodels.dto.product.search.ProductSearchResponseDTO;
import com.nixagh.classicmodels.dto.statistical.request.ProductsEachMonthInYear;
import com.nixagh.classicmodels.dto.statistical.request.StatisticDTO;
import com.nixagh.classicmodels.dto.statistical.request.StatisticalRequest;
import com.nixagh.classicmodels.dto.statistical.response.ProductEachMonth;
import com.nixagh.classicmodels.dto.statistical.response.ProductStatisticResponse;
import com.nixagh.classicmodels.dto.statistical.response.Top10ProductResponse;
import com.nixagh.classicmodels.entity.Product;
import com.nixagh.classicmodels.entity.ProductLinee;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IProductService {
    List<Top10ProductResponse> getTop10Products(Date from, Date to);

    Map<Integer, ProductsEachMonthInYear> getProductsEachMonthInYear(int year);

    StatisticDTO getAllStatistical();

    List<Product> getProducts();

    ProductStatisticResponse getProductStatistical(StatisticalRequest statisticalRequest);

    ProductEachMonth getProductEachMonth(int year, int month, long pageNumber, long pageSize);

    Map<String, String> addProduct(ProductAddRequest product);

    ProductSearchResponse filterProducts(ProductSearchRequest request);

    Map<String, String> deleteProduct(String productCode);

    ProductSearchResponseDTO getProduct(String productCode);

    Map<String, String> updateProduct(String productCode, ProductUpdateRequest product);

    ProductLinee checkField(String productLine, Integer productScale, String productVendor, Integer quantityInStock);

    ProductSearchResponse managerSearch(ProductManagerSearchRequest request);

    List<ProductController.ProductOutOfStockResponse> getOutOfStockProducts();

    Map<String, String> updateQuantityInStock(String productCode, Integer quantityInStock);

    StatisticalController.ByteArrayInputStreamResponse getExportProduct(int year, int month)
            throws IOException, NoSuchFieldException, IllegalAccessException;

    OverviewTotal getTotalSoldProductAndProfit(String from, String to);

    OverviewTop.Product getTop1Product(String from, String to);

    OverviewTop.ProductLine getTop1ProductLine(String from, String to);
}
