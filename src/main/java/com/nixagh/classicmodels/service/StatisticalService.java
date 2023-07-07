package com.nixagh.classicmodels.service;

import com.nixagh.classicmodels.dto.DateRange;
import com.nixagh.classicmodels.dto.statistical.*;
import com.nixagh.classicmodels.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticalService {

    private final OrderService orderService;
    private final ProductService productService;

    public StatisticalResponse getStatistical(StatisticalRequest statisticalRequest) {
        StatisticalResponse response = new StatisticalResponse();
        // set time range
        response.setTimeRange(new DateRange(statisticalRequest.getFrom(), statisticalRequest.getTo()));

        // get page infos from request
        long pageSize = statisticalRequest.getPageInfo().getPageSize() == 0 ? 10 : statisticalRequest.getPageInfo().getPageSize();
        long pageNumber = statisticalRequest.getPageInfo().getPageNumber() == 0 ? 1 : statisticalRequest.getPageInfo().getPageNumber();

        // get orders by time range and pagination
        List<OrderWithProfit> orders = orderService.getOrderByTimeRange(statisticalRequest.getFrom(), statisticalRequest.getTo());
        List<OrderWithProfit> order_ = orders.stream().skip(pageSize * (pageNumber - 1)).limit(pageSize).toList();

        // create order statistic info
        OrderStatistic orderStatistic = new OrderStatistic();
        orderStatistic.setTotalOrder((long) orders.size());
        orderStatistic.setOrders(order_);
        orderStatistic.setPageResponseInfo(PageUtil.getResponse(pageNumber, pageSize, (long) orders.size(), (long) order_.size()));

        // set response
        response.setOrder(orderStatistic);
        response.setTotalProfit(orders.stream().map(OrderWithProfit::getProfit).reduce(0.00, Double::sum));
        return response;
    }

    public List<Top10ProductResponse> getTop10Products(Date from, Date to) {
        return productService.getTop10Products(from, to);
    }

    public Map<Integer, Double> getProfitEachMonthInYear(int year) {
        return orderService.getProfitEachMonthInYear(year);
    }

    public Map<Integer, ProductsEachMonthInYear> getProductsEachMonthInYear(int year) {
        return productService.getProductsEachMonthInYear(year);
    }
}
