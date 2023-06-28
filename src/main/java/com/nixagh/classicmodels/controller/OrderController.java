package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels.dto.orderDetail.OrderDetailByOrderNumber;
import com.nixagh.classicmodels.dto.orders.*;
import com.nixagh.classicmodels.entity.Order;
import com.nixagh.classicmodels.service.OrderService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class OrderController {
  private final OrderService orderService;

  @PostMapping("/filters")
  public OrderSearchResponse getOrderByFilters(@RequestBody OrderFilterRequest request) {
    return orderService.getOrderByFilters(request);
  }

  @PostMapping("/save")
  public Order saveOrder(@RequestBody OrderCreateRequest request) {
    return orderService.saveOrder(request);
  }

  @GetMapping("/{orderNumber}/orderDetail")
  public OrderDetailByOrderNumber getDetail(@PathVariable(value = "orderNumber") Long orderNumber) {
    return orderService.getOrderDetail(orderNumber);
  }

  @PutMapping("/{orderNumber}")
  public OrderUpdateResponse getDetail(
          @PathVariable(value = "orderNumber") Long orderNumber,
          @RequestBody OrderUpdateRequest request) {
    return orderService.updateOrder(orderNumber, request);
  }

  @DeleteMapping("/{orderNumber}")
  public OrderDeleteResponse deleteOrder(@PathVariable(value = "orderNumber") Long orderNumber) {
    return orderService.deleteOrder(orderNumber);
  }

  @GetMapping("/{orderNumber}")
  public OrderGetResponse getOrder(@PathVariable(value = "orderNumber") Long orderNumber ) {
    return orderService.getOrder(orderNumber);
  }

}
