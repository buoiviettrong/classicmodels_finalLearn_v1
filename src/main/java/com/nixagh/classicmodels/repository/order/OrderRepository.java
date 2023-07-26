package com.nixagh.classicmodels.repository.order;

import com.nixagh.classicmodels.dto.orders.HighestOrderResponse;
import com.nixagh.classicmodels.dto.orders.OrderFilter;
import com.nixagh.classicmodels.entity.Order;
import com.nixagh.classicmodels.repository.BaseRepository;
import com.querydsl.core.Tuple;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends BaseRepository<Order, Long> {
    List<Order> getOrderByCustomerNumber(Long customerNumber);

    List<Order> getOrders();

    Order getOrderByOrderNumber(Long orderNumber);

    List<Order> getOrderByFilters(OrderFilter filter, Long page, Long size);

    Long getCount(OrderFilter request);

    Optional<Order> findOrderByOrderNumber(Long orderNumber);

    void deleteByOrderNumber(Long orderNumber);

    HighestOrderResponse getHighestOrder();

    List<Tuple> getOrderByTimeRange(Date from, Date to);

    List<Tuple> getOrderStatistical(Date from, Date to, long offset, long limit);

    long countOrderStatistical(Date from, Date to);

    void updateStatus(Long orderNumber, String status);

    List<Tuple> getOrderDetails(Integer year, Integer month, String status, Long offset, Long limit);

    List<Tuple> getStatusMap(Integer year, Integer month);

    List<Tuple> countOrderDetails(Integer year, Integer month);

    Long getTotalOrder(java.util.Date from, java.util.Date to);

    Tuple getTop1Order(java.util.Date from, java.util.Date to);

    List<Tuple> getOrderByEachTime(java.util.Date from, java.util.Date to);
}
