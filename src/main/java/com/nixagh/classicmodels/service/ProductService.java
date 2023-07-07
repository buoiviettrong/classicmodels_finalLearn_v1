package com.nixagh.classicmodels.service;

import com.nixagh.classicmodels.dto.statistical.Top10ProductResponse;
import com.nixagh.classicmodels.repository.ProductNoDSLRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductNoDSLRepository productNoDSLRepository;

    public List<Top10ProductResponse> getTop10Products(Date from, Date to) {
        return productNoDSLRepository.getTop10Products(from, to).stream().map(
                tuple -> Top10ProductResponse.builder()
                        .productCode(tuple.get("productCode", String.class))
                        .totalSoldQuantity(tuple.get("totalSoldQuantity", BigDecimal.class).longValue())
                        .totalProfit(tuple.get("totalProfit", Double.class))
                        .build()
        ).toList();
    }
}
