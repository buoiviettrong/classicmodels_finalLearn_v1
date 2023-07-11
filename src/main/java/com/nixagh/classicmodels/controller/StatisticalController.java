package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels.dto.statistical.*;
import com.nixagh.classicmodels.service.StatisticalService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/statistical")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class StatisticalController {
    private final StatisticalService statisticalService;

    @PostMapping
    public StatisticalResponse getStatistical(@RequestBody StatisticalRequest statisticalRequest) {
        System.out.println(statisticalRequest);
        return statisticalService.getStatistical(statisticalRequest);
    }

    @GetMapping("/products/top10")
    public List<Top10ProductResponse> getTop10Products(@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                       @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
        return statisticalService.getTop10Products(from, to);
    }

    @GetMapping
    public Map<Integer, Double> getProfitEachMonthInYear(@RequestParam("year") int year) {
        return statisticalService.getProfitEachMonthInYear(year);
    }

    @GetMapping("/products/each-month")
    public Map<Integer, ProductsEachMonthInYear> getProductsEachMonthInYear(@RequestParam("year") int year) {
        return statisticalService.getProductsEachMonthInYear(year);
    }

    @GetMapping("/all")
    public StatisticDTO getAllStatistical() {
        return statisticalService.getAllStatistical();
    }

    @PostMapping("/customers")
    public CustomerStatisticResponse getCustomerStatistical(@RequestBody StatisticalRequest statisticalRequest) {
        return statisticalService.getCustomerStatistical(statisticalRequest);
    }

    @PostMapping("/products")
    public ProductStatisticResponse getProductStatistical(@RequestBody StatisticalRequest statisticalRequest) {
        System.out.println(statisticalRequest);
        return statisticalService.getProductStatistical(statisticalRequest);
    }

    @PostMapping("/orders")
    public OrderStatisticResponse getOrderStatistical(@RequestBody StatisticalRequest statisticalRequest) {
        return statisticalService.getOrderStatistical(statisticalRequest);
    }

    @PostMapping("/orders/status")
    public List<OrderStatusStatisticResponse> getOrderStatusStatistical(@RequestBody StatisticalRequest statisticalRequest) {
        return statisticalService.getOrderStatusStatistical(statisticalRequest);
    }
}
