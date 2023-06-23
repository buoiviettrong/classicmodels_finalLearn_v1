package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.dto.orders.OrderFilter;
import com.nixagh.classicmodels.dto.orders.OrderFilterRequest;
import com.nixagh.classicmodels.dto.orders.OrderSearchResponse;
import com.nixagh.classicmodels.entity.Order;

import java.util.List;

public interface OrderRepository extends BaseRepository<Order, Long>{
	public List<Order> getOrderByCustomerNumber(Long customerNumber);
	public List<Order> getOrders();

	public Order getOrderByOrderNumber(Long orderNumber);

	public List<Order> getOrderByFilters(OrderFilter filter, Long page, Long size);
	public Long getCount(OrderFilter request);
}
