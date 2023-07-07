package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels.dto.statistical.StatisticalRequest;
import com.nixagh.classicmodels.dto.statistical.StatisticalResponse;
import com.nixagh.classicmodels.dto.statistical.Top10ProductResponse;
import com.nixagh.classicmodels.service.StatisticalService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
}
