package com.nixagh.classicmodels.service;

import com.nixagh.classicmodels.dto.statistical.*;
import com.nixagh.classicmodels.entity.Product;
import com.nixagh.classicmodels.repository.ProductNoDSLRepository;
import com.nixagh.classicmodels.repository.ProductRepository;
import com.nixagh.classicmodels.utils.PageUtil;
import com.querydsl.core.Tuple;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductNoDSLRepository productNoDSLRepository;
    private final EntityManager entityManager;
    private final ProductRepository productRepository;

    public List<Top10ProductResponse> getTop10Products(Date from, Date to) {
        return productNoDSLRepository.getTop10Products(from, to).stream().map(
                tuple -> Top10ProductResponse.builder()
                        .productCode(tuple.get("productCode", String.class))
                        .productName(tuple.get("productName", String.class))
                        .totalSoldQuantity(tuple.get("totalSoldQuantity", BigDecimal.class).longValue())
                        .totalProfit(tuple.get("totalProfit", Double.class))
                        .build()
        ).toList();
    }

    public Map<Integer, ProductsEachMonthInYear> getProductsEachMonthInYear(int year) {
        // tạo map với key là tháng, value là ProductsEachMonthInYear (chứa danh sách sản phẩm và tổng profit của tháng đó)
        Map<Integer, ProductsEachMonthInYear> products = new HashMap<>();
        // thêm 12 tháng vào map với value là ProductsEachMonthInYear rỗng
        for (int i = 1; i <= 12; i++) products.put(i, new ProductsEachMonthInYear());

        // lấy danh sách các tuple (tháng, productCode, productName, totalSoldQuantity, totalProfit)
        List<Tuple> tuples = productRepository.findProductsEachMonthInYear_(year);

        // duyệt danh sách các tuple và thêm vào map
        tuples.forEach(tuple -> {
            Integer month = tuple.get(0, Integer.class);
            ProductsEachMonthInYear list = products.get(month);
            Top10ProductResponse product = Top10ProductResponse.builder()
                    .productCode(tuple.get(1, String.class))
                    .productName(tuple.get(2, String.class))
                    .totalSoldQuantity(tuple.get(3, Long.class))
                    .totalProfit(tuple.get(4, Double.class))
                    .build();
            list.getProducts().add(product);
            products.put(month, list);
        });
        // tính tổng profit của mỗi tháng
        products.forEach((key, value) -> {
            value.setTotalProfit(value.getProducts().stream().mapToDouble(Top10ProductResponse::getTotalProfit).sum());
        });
        // trả về map
        return products;
    }

    public StatisticDTO getAllStatistical() {
        StatisticDTO statisticDTO = new StatisticDTO();
        // lấy danh sách các tuple (tháng, customerNumber, customerName,  productCode, productName, totalSoldQuantity, totalProfit)
        List<Tuple> tuples = productRepository.findAllStatistic();

        // duyệt danh sách các tuple và thêm vào StatisticDTO

        return statisticDTO;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public ProductStatisticResponse getProductStatistical(StatisticalRequest statisticalRequest) {
        ProductStatisticResponse productStatisticResponse = new ProductStatisticResponse();

        Date from = statisticalRequest.getFrom();
        Date to = statisticalRequest.getTo();

        if (from == null) {
            from = new Date(0);
        }
        if (to == null) {
            to = new Date();
        }

        long pageNumber = statisticalRequest.getPageInfo().getPageNumber();
        long pageSize = statisticalRequest.getPageInfo().getPageSize();

        long offset = pageSize * (pageNumber - 1);

        List<ProductStatisticDTO> products = productNoDSLRepository.getProductStatistical(from, to, offset, pageSize)
                .stream()
                .map(tuple -> ProductStatisticDTO.builder()
                        .productCode(tuple.get("productCode", String.class))
                        .productName(tuple.get("productName", String.class))
                        .totalSoldQuantity(tuple.get("totalSoldQuantity", BigDecimal.class).longValue())
                        .totalAmount(tuple.get("totalAmount", Double.class))
                        .build())
                .toList();
        long totalItems = productNoDSLRepository.countProductStatistical(from, to);

        productStatisticResponse.setProducts(products);
        productStatisticResponse.setPageResponseInfo(PageUtil.getResponse(pageNumber, pageSize, totalItems, (long) products.size()));

        return productStatisticResponse;
    }
}
