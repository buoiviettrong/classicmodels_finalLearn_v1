package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels.dto.AbstractRequest;
import com.nixagh.classicmodels.dto.AbstractResponse;
import com.nixagh.classicmodels.dto.orders.OrderCreateRequest;
import com.nixagh.classicmodels.dto.orders.OrderFilterRequest;
import com.nixagh.classicmodels.dto.orders.OrderSearchResponse;
import com.nixagh.classicmodels.entity.Order;
import com.nixagh.classicmodels.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;

	@PostMapping("/filters")
	public OrderSearchResponse getOrderByFilters(@RequestBody OrderFilterRequest request) {
		System.out.println(request.getOrderFilter());
		return orderService.getOrderByFilters(request);
	}

//	@GetMapping("")
//	public List<Order> getOrders() {
//		return orderService.getOrders();
//	}

	@PostMapping("/save")
	public Order saveOrder(@RequestBody OrderCreateRequest request) {
    return orderService.saveOrder(request);
  }

}
