package com.nixagh.classicmodels.dto.statistical.request;

import com.nixagh.classicmodels.dto.statistical.response.Top10ProductResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductsEachMonthInYear {
    private Double totalProfit = (double) 0;
    private List<Top10ProductResponse> products = new ArrayList<>();
}
