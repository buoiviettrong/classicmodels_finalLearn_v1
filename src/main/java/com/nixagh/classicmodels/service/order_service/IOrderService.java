package com.nixagh.classicmodels.service.order_service;

import com.nixagh.classicmodels.dto._statistic.synthetic.details.Details;
import com.nixagh.classicmodels.dto._statistic.synthetic.overview.OverviewTop;
import com.nixagh.classicmodels.dto.orders.*;
import com.nixagh.classicmodels.dto.orders.admin.statictis.customer.orders.detail.CustomerOrderDetailResponse;
import com.nixagh.classicmodels.dto.orders.admin.statictis.order.OrderDetailResponse;
import com.nixagh.classicmodels.dto.orders.manager.history.OrderHistoryResponse;
import com.nixagh.classicmodels.dto.product.ProductDTO;
import com.nixagh.classicmodels.dto.statistical.request.OrderWithProfit;
import com.nixagh.classicmodels.dto.statistical.request.StatisticalRequest;
import com.nixagh.classicmodels.dto.statistical.response.OrderEachMonth;
import com.nixagh.classicmodels.dto.statistical.response.OrderStatisticResponse;
import com.nixagh.classicmodels.dto.statistical.response.OrderStatusStatisticResponse;
import com.nixagh.classicmodels.entity.Order;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IOrderService {
    List<Order> getOrderByCustomerNumber(Long customerNumber);

    List<Order> getOrders();

    OrderSearchResponse getOrderByFilters(OrderFilterRequest request);

    Order saveOrder(OrderCreateRequest order);

    List<ProductDTO> getOrderDetailByOrderNumber(Long orderNumber);

    Order updateOrder(Long orderNumber, OrderUpdateRequest request);

    long deleteOrder(Long orderNumber);

    Order getOrderByOrderNumber(Long orderNumber);

    Map<Integer, Double> getProfitEachMonthInYear(int year);

    OrderStatisticResponse getOrderStatistical(StatisticalRequest statisticalRequest);

    List<OrderStatusStatisticResponse> getOrderStatusStatistical(StatisticalRequest statisticalRequest);

    List<OrderEachMonth> getOrderEachMonth(int year);

    List<OrderHistoryResponse> getHistory(Long customerNumber);

    Map<String, String> changeStatus(Long orderNumber, String status);

    List<CustomerOrderDetailResponse> getOrderDetailByCustomerNumber(Long customerNumber, Integer year, Integer month);

    OrderDetailResponse getOrderDetails(Integer year, Integer month, String status, Long pageNumber, Long pageSize);

    HighestOrderResponse getHighestOrder();

    List<OrderWithProfit> getOrderByTimeRange(@DateTimeFormat(pattern = "yyyy-MM-dd") java.sql.Date from,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd") java.sql.Date to);

    Long getTotalOrder(Date from, Date to);

    OverviewTop.Invoice getTop1Order(Date from, Date to);

    List<Details> getOrderByEachTime(Date from, Date to);
}
