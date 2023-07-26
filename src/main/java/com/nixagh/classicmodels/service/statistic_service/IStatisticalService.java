package com.nixagh.classicmodels.service.statistic_service;

import com.nixagh.classicmodels.controller.StatisticalController;
import com.nixagh.classicmodels.dto._statistic.Details.DetailsStatisticRequest;
import com.nixagh.classicmodels.dto._statistic.Details.DetailsStatisticResponse;
import com.nixagh.classicmodels.dto._statistic.Synthetic.SyntheticStatisticRequest;
import com.nixagh.classicmodels.dto._statistic.Synthetic.SyntheticStatisticResponse;
import com.nixagh.classicmodels.dto.statistical.request.ProductsEachMonthInYear;
import com.nixagh.classicmodels.dto.statistical.request.StatisticDTO;
import com.nixagh.classicmodels.dto.statistical.request.StatisticalRequest;
import com.nixagh.classicmodels.dto.statistical.response.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IStatisticalService {
    StatisticalResponse getStatistical(StatisticalRequest statisticalRequest);

    List<Top10ProductResponse> getTop10Products(Date from, Date to);

    Map<Integer, Double> getProfitEachMonthInYear(int year);

    Map<Integer, ProductsEachMonthInYear> getProductsEachMonthInYear(int year);

    StatisticDTO getAllStatistical();

    CustomerStatisticResponse getCustomerStatistical(StatisticalRequest statisticalRequest);

    ProductStatisticResponse getProductStatistical(StatisticalRequest statisticalRequest);

    OrderStatisticResponse getOrderStatistical(StatisticalRequest statisticalRequest);

    List<OrderStatusStatisticResponse> getOrderStatusStatistical(StatisticalRequest statisticalRequest);

    List<OrderEachMonth> getOrderEachMonth(int year);

    ProductEachMonth getProductEachMonth(int year, int month, int pageNumber, int pageSize);

    CustomerEachMonth getCustomerEachMonth(String customerName, int year, int month, int pageNumber, int pageSize);

    StatisticalController.ByteArrayInputStreamResponse getExportProduct(int year, int month)
            throws IOException, NoSuchFieldException, IllegalAccessException;

    SyntheticStatisticResponse getSyntheticStatistic(SyntheticStatisticRequest syntheticStatisticRequest);

    DetailsStatisticResponse getDetailStatisticDetail(DetailsStatisticRequest detailsStatisticRequest, Long pageNumber, Long pageSize);
}
