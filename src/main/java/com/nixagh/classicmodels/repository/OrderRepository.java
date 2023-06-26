package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.dto.orders.OrderFilter;
import com.nixagh.classicmodels.entity.Order;

import java.util.List;

public interface OrderRepository extends BaseRepository<Order, Long> {
  List<Order> getOrderByCustomerNumber(Long customerNumber);

  List<Order> getOrders();

  Order getOrderByOrderNumber(Long orderNumber);

  List<Order> getOrderByFilters(OrderFilter filter, Long page, Long size);

  Long getCount(OrderFilter request);
}
