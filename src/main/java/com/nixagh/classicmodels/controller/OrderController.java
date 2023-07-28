package com.nixagh.classicmodels.controller;

import com.nixagh.classicmodels.dto.orders.*;
import com.nixagh.classicmodels.dto.orders.admin.AdminOrderResponse;
import com.nixagh.classicmodels.dto.orders.admin.statictis.customer.orders.detail.CustomerOrderDetailResponse;
import com.nixagh.classicmodels.dto.orders.admin.statictis.order.OrderDetailResponse;
import com.nixagh.classicmodels.dto.orders.manager.history.OrderHistoryResponse;
import com.nixagh.classicmodels.dto.product.ProductDTO;
import com.nixagh.classicmodels.entity.Order;
import com.nixagh.classicmodels.service.order_service.IOrderService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
@EnableCaching
@Slf4j
public class OrderController {
    private final IOrderService orderService;
    private final String cacheName = "orders";
    private final String cacheFilter = "ordersFilter";
    private final String cacheDetail = "ordersDetail";

    @PostMapping("/filters")
    @Cacheable(value = cacheFilter, key = "#request.toString()", cacheManager = "cacheManager", unless = "#result == null")
    public OrderSearchResponse getOrderByFilters(@RequestBody OrderFilterRequest request) {
        return orderService.getOrderByFilters(request);
    }

    @PostMapping("/save")
    public Order saveOrder(@RequestBody OrderCreateRequest request) {
        return orderService.saveOrder(request);
    }

    @PostMapping("/checkout")
    public Order checkout(@RequestBody OrderCreateRequest request) {
        return orderService.saveOrder(request);
    }

    @GetMapping("/{orderNumber}/orderDetail")
    @Cacheable(value = cacheDetail, key = "#orderNumber.toString()", cacheManager = "cacheManager", unless = "#result == null")
    public List<ProductDTO> getDetail(@PathVariable(value = "orderNumber") Long orderNumber) {
        return orderService.getOrderDetailByOrderNumber(orderNumber);
    }

    @PutMapping("/{orderNumber}")
    public Order getDetail(
            @PathVariable(value = "orderNumber") Long orderNumber,
            @RequestBody OrderUpdateRequest request) {
        return orderService.updateOrder(orderNumber, request);
    }

    @DeleteMapping("/{orderNumber}")
    @CacheEvict(value = {cacheName, cacheFilter, cacheDetail}, allEntries = true)
    public long deleteOrder(@PathVariable(value = "orderNumber") Long orderNumber) {
        return orderService.deleteOrder(orderNumber);
    }

    @GetMapping("/{orderNumber}")
    @Cacheable(value = cacheName, key = "#orderNumber.toString()", cacheManager = "cacheManager", unless = "#result == null")
    public Order getOrder(@PathVariable(value = "orderNumber") Long orderNumber) {
        return orderService.getOrderByOrderNumber(orderNumber);
    }

    @GetMapping("/highestOrder")
    public HighestOrderResponse getHighestOrder() {
        return orderService.getHighestOrder();
    }

    @GetMapping("/history")
    public List<OrderHistoryResponse> getHistory(@PathParam(value = "customerNumber") Long customerNumber) {
        // check SQL injection
        return orderService.getHistory(customerNumber);
    }

    @PostMapping("/change-status")
    public Map<String, String> changeStatus(@RequestBody changeStatus request) {
        return orderService.changeStatus(request.orderNumber, request.status);
    }

    @GetMapping("/customer-order-details")
    public List<CustomerOrderDetailResponse> getCustomerOrderDetails(
            @PathParam(value = "customerNumber") Long customerNumber,
            @PathParam(value = "year") Integer year,
            @PathParam(value = "month") Integer month
    ) {
        return orderService.getOrderDetailByCustomerNumber(customerNumber, year, month);
    }

    @GetMapping("/order-details")
    public OrderDetailResponse getOrderDetails(
            @PathParam(value = "year") Integer year,
            @PathParam(value = "month") Integer month,
            @PathParam(value = "status") String status,
            @PathParam(value = "pageNumber") Long pageNumber,
            @PathParam(value = "pageSize") Long pageSize
    ) {
        return orderService.getOrderDetails(year, month, status, pageNumber, pageSize);
    }

    private record changeStatus(Long orderNumber, String status) {
    }

    @GetMapping("/admin")
    public AdminOrderResponse getAdminOrder(
            @PathParam(value = "status") String status,
            @PathParam(value = "paymentStatus") String paymentStatus,
            @PathParam(value = "fromDate") String fromDate,
            @PathParam(value = "toDate") String toDate,
            @PathParam(value = "pageNumber") Long pageNumber,
            @PathParam(value = "pageSize") Long pageSize
    ) {
        System.out.println("getAdminOrder");
        System.out.println("status: " + status);
        System.out.println("paymentStatus: " + paymentStatus);
        
        return orderService.getAdminOrder(status, paymentStatus, fromDate, toDate, pageNumber, pageSize);
    }
}
