package com.nixagh.classicmodels.service;

import com.nixagh.classicmodels.dto.PageRequestInfo;
import com.nixagh.classicmodels.dto.PageResponseInfo;
import com.nixagh.classicmodels.dto.orders.*;
import com.nixagh.classicmodels.dto.orders.manager.history.OrderHistoryResponse;
import com.nixagh.classicmodels.dto.product.ProductDTO;
import com.nixagh.classicmodels.dto.statistical.request.OrderStatisticDTO;
import com.nixagh.classicmodels.dto.statistical.request.OrderWithProfit;
import com.nixagh.classicmodels.dto.statistical.request.StatisticalRequest;
import com.nixagh.classicmodels.dto.statistical.response.OrderEachMonth;
import com.nixagh.classicmodels.dto.statistical.response.OrderStatisticResponse;
import com.nixagh.classicmodels.dto.statistical.response.OrderStatusStatisticResponse;
import com.nixagh.classicmodels.entity.Customer;
import com.nixagh.classicmodels.entity.Order;
import com.nixagh.classicmodels.entity.OrderDetail;
import com.nixagh.classicmodels.entity.Product;
import com.nixagh.classicmodels.entity.embedded.OrderDetailsEmbed;
import com.nixagh.classicmodels.entity.enums.ShippingStatus;
import com.nixagh.classicmodels.exception.NotEnoughProduct;
import com.nixagh.classicmodels.exception.NotFoundEntity;
import com.nixagh.classicmodels.exception.NotSupportStatus;
import com.nixagh.classicmodels.exception.PageInfoException;
import com.nixagh.classicmodels.repository.*;
import com.nixagh.classicmodels.utils.PageUtil;
import com.nixagh.classicmodels.utils.RoundUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final OrderNoDSLRepository orderNoDSLRepository;

    public List<Order> getOrderByCustomerNumber(Long customerNumber) {
        return orderRepository.getOrderByCustomerNumber(customerNumber);
    }

    public List<Order> getOrders() {
        return orderRepository.getOrders();
    }

    public OrderSearchResponse getOrderByFilters(OrderFilterRequest request) {
        OrderSearchResponse orderSearchResponse = new OrderSearchResponse();
        PageRequestInfo pageRequestInfo = request.getPageInfo();
        OrderFilter orderFilter = request.getOrderFilter();

        if (pageRequestInfo.getPageNumber() < 1)
            throw new PageInfoException("Page number must be greater than 0");
        if (pageRequestInfo.getPageSize() < 1)
            throw new PageInfoException("Page size must be greater than 0");

        List<Order> orders = orderRepository.getOrderByFilters(orderFilter, pageRequestInfo.getPageNumber(), pageRequestInfo.getPageSize());
        PageResponseInfo pageResponseInfo = PageUtil.getResponse(
                pageRequestInfo.getPageNumber(),
                pageRequestInfo.getPageSize(),
                orderRepository.getCount(orderFilter),
                (long) orders.size()
        );

        orderSearchResponse.setPageResponseInfo(pageResponseInfo);
        orderSearchResponse.setOrders(orders);

        return orderSearchResponse;
    }

    @Transactional
    public Order saveOrder(OrderCreateRequest order) {
        Customer customer = customerRepository.findByCustomerNumber(order.getCustomerNumber());

        Order saveOrder = Order.builder()
                .orderDate(new Date())
                .requiredDate(order.getRequireDate())
                .customer(customer)
                .comments("")
                .status(ShippingStatus.INPROC.getShippingStatus())
                .build();

        // tạo order mới
        var result = orderRepository.save(saveOrder);

        // tạo order detail
        List<ProductOrder> productOrders = order.getProducts();
        for (int i = 0; i < productOrders.size(); i++) {
            Product product_ = productRepository.findProductByProductCode(productOrders.get(i).getProductCode());
            if (product_ == null)
                throw new NotFoundEntity("Product not found");

            if (product_.getQuantityInStock() < productOrders.get(i).getQuantity())
                throw new NotEnoughProduct("Product %s not enough quantity".formatted(product_.getProductName()));

            OrderDetail orderDetail = OrderDetail.builder()
                    .id(new OrderDetailsEmbed(result.getOrderNumber(), product_.getProductCode()))
                    .order(saveOrder)
                    .product(product_)
                    .quantityOrdered(productOrders.get(i).getQuantity())
                    .priceEach(product_.getMsrp())
                    .orderLineNumber(i + 1)
                    .build();
            orderDetailRepository.save(orderDetail);
        }

        // cập nhập số lượng sản phẩm trong kho product
        for (ProductOrder productOrder : productOrders) {
            Product product_ = productRepository.findProductByProductCode(productOrder.getProductCode());
            product_.setQuantityInStock((int) (product_.getQuantityInStock() - productOrder.getQuantity()));
            productRepository.save(product_);
        }

        return result;
    }

    public List<ProductDTO> getOrderDetail(Long orderNumber) {
        List<OrderDetail> orderDetailList = orderDetailRepository.getOrderDetail(orderNumber);

        return orderDetailList.stream()
                .map(item -> {
                    Product product = item.getProduct();
                    return new ProductDTO(
                            product.getProductCode(),
                            product.getProductName(),
                            product.getProductVendor(),
                            product.getProductDescription(),
                            item.getQuantityOrdered(),
                            item.getPriceEach()
                    );
                })
                .collect(Collectors.toList());
    }

    public Order updateOrder(Long orderNumber, OrderUpdateRequest request) {
        Order order = orderRepository.findOrderByOrderNumber(orderNumber).orElseThrow(() -> new NotFoundEntity("Order not found"));

        if (request.getStatus() != null) {
            ShippingStatus status;
            try {
                status = ShippingStatus.valueOf(request.getStatus());
            } catch (IllegalArgumentException e) {
                throw new NotSupportStatus("status %s not supported".formatted(request.getStatus()));
            }

            switch (status) {
                case SHIPPED -> {
                    order.setStatus(ShippingStatus.SHIPPED.getShippingStatus());
                    order.setShippedDate(new Date());
                }
                case CANCELLED -> order.setStatus(ShippingStatus.CANCELLED.getShippingStatus());
                case DELIVERING -> order.setStatus(ShippingStatus.DELIVERING.getShippingStatus());
                case DELIVERED -> order.setStatus(ShippingStatus.DELIVERED.getShippingStatus());
                case RECEIVED -> order.setStatus(ShippingStatus.RECEIVED.getShippingStatus());
                case RESOLVED -> order.setStatus(ShippingStatus.RESOLVED.getShippingStatus());
                case INPROC -> order.setStatus(ShippingStatus.INPROC.getShippingStatus());
                case DISPUTED -> order.setStatus(ShippingStatus.DISPUTED.getShippingStatus());
                case HOLD -> order.setStatus(ShippingStatus.HOLD.getShippingStatus());
            }
        }

        if (request.getShippedDate() != null) order.setShippedDate(request.getShippedDate());

        if (request.getComment() != null) order.setComments(request.getComment());

        orderRepository.save(order);

        // nếu status là canceler thì cập nhập số lượng sản phẩm trong kho product
        if (order.getStatus().equals(ShippingStatus.CANCELLED.getShippingStatus())) {
            List<OrderDetail> orderDetailList = orderDetailRepository.getOrderDetail(orderNumber);
            for (OrderDetail orderDetail : orderDetailList) {
                Product product_ = orderDetail.getProduct();
                product_.setQuantityInStock((int) (product_.getQuantityInStock() + orderDetail.getQuantityOrdered()));
                productRepository.save(product_);
            }
        }

        return order;
    }

    public long deleteOrder(Long orderNumber) {
        Order order = orderRepository.findOrderByOrderNumber(orderNumber).orElseThrow(() -> new NotFoundEntity("Order not found"));

        // delete order details
        orderDetailRepository.deleteByOrderNumber(orderNumber);
        // delete order
        orderRepository.deleteByOrderNumber(orderNumber);

        return order.getOrderNumber();
    }

    public Order getOrder(Long orderNumber) {
        return orderRepository.findOrderByOrderNumber(orderNumber).orElseThrow(() -> new NotFoundEntity("Order " + orderNumber + " not found"));
    }

    public HighestOrderResponse getHighestOrder() {
        return orderRepository.getHighestOrder();
    }

    public List<OrderWithProfit> getOrderByTimeRange(@DateTimeFormat(pattern = "yyyy-MM-dd") java.sql.Date from,
                                                     @DateTimeFormat(pattern = "yyyy-MM-dd") java.sql.Date to) {
        // giảm 1 ngày để lấy đủ dữ liệu
        long oneDay = 24 * 60 * 60 * 1000;
        java.sql.Date from_ = new java.sql.Date(from.getTime() - oneDay);

        return orderNoDSLRepository.getOrderByTimeRange(from, to)
                .stream()
                .map(tuple -> OrderWithProfit.builder()
                        // get order detail
                        .order(Order.builder()
                                .orderNumber(Long.valueOf(tuple.get("orderNumber", Integer.class)))
                                .orderDate(tuple.get("orderDate", Date.class))
                                .requiredDate(tuple.get("requiredDate", Date.class))
                                .shippedDate(tuple.get("shippedDate", Date.class))
                                .status(tuple.get("status", String.class))
                                .comments(tuple.get("comments", String.class))
                                .build())
                        .profit(tuple.get("totalProfit", Double.class))
                        .build()
                ).toList();
    }

    public Map<Integer, Double> getProfitEachMonthInYear(int year) {
        Map<Integer, Double> map = new HashMap<>();
        for (int i = 1; i <= 12; i++) map.put(i, 0.0);

        orderNoDSLRepository.getProfitEachMonthInYear(year)
                .forEach(tuple -> map.put(tuple.get("month", Integer.class), tuple.get("profit", Double.class)));
        return map;
    }

    public OrderStatisticResponse getOrderStatistical(StatisticalRequest statisticalRequest) {
        long offset = (statisticalRequest.getPageInfo().getPageNumber() - 1) * statisticalRequest.getPageInfo().getPageSize();
        long limit = statisticalRequest.getPageInfo().getPageSize();

        List<OrderStatisticDTO> orders = orderRepository.getOrderStatistical(statisticalRequest.getFrom(), statisticalRequest.getTo(), offset, limit)
                .stream()
                .map(tuple -> OrderStatisticDTO.builder()
                        .orderNumber(tuple.get(0, Long.class))
                        .orderDate(tuple.get(1, Date.class))
                        .shippedDate(tuple.get(2, Date.class))
                        .status(tuple.get(3, String.class))
                        .customerNumber(tuple.get(4, Long.class))
                        .comment(tuple.get(5, String.class))
                        .build())
                .toList();
        long total = orderRepository.countOrderStatistical(statisticalRequest.getFrom(), statisticalRequest.getTo());

        OrderStatisticResponse response = new OrderStatisticResponse();
        response.setOrders(orders);
        response.setPageResponseInfo(PageUtil.getResponse(statisticalRequest.getPageInfo().getPageNumber(), statisticalRequest.getPageInfo().getPageSize(), total, (long) orders.size()));

        return response;
    }

    public List<OrderStatusStatisticResponse> getOrderStatusStatistical(StatisticalRequest statisticalRequest) {
        return orderNoDSLRepository.getOrderStatusStatistical(statisticalRequest.getFrom(), statisticalRequest.getTo())
                .stream()
                .map(tuple -> OrderStatusStatisticResponse.builder()
                        .status(tuple.get("status", String.class))
                        .count(tuple.get("count", Long.class))
                        .amount(tuple.get("profit", Double.class))
                        .build())
                .toList();
    }

    public List<OrderEachMonth> getOrderEachMonth(int year) {
        // tạo ra 12 tháng
        List<OrderEachMonth> orderEachMonths = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            orderEachMonths.add(new OrderEachMonth(i,
                    0L,
                    0L,
                    0L,
                    0L,
                    0D,
                    0D,
                    0D,
                    0D));
        }
        orderNoDSLRepository.getOrderEachMonth(year)
                .forEach(tuple -> {
                    // lấy dữ liệu
                    int month = tuple.get("month", Long.class).intValue();
                    String status = tuple.get("status", String.class);
                    Double profit = tuple.get("profit", Double.class);

                    // lấy ra tháng tương ứng
                    var order = orderEachMonths.get(month - 1);
                    // thêm dữ liệu vào tháng tương ứng theo status
                    switch (status) {
                        // trạng thái order đã được xác nhận
                        case "Shipped" -> {
                            order.setSuccessOrder(order.getSuccessOrder() + 1);
                            order.setSuccessProfit(RoundUtil.convert(order.getSuccessProfit() + profit, 2));
                        }
                        // trạng thái order đã bị hủy
                        case "Cancelled" -> {
                            order.setCancellerOrder(order.getCancellerOrder() + 1);
                            order.setCancellerProfit(RoundUtil.convert(order.getCancellerProfit() + profit, 2));
                        }
                        // trạng thái order khác
                        default -> {
                            order.setOtherOrder(order.getOtherOrder() + 1);
                            order.setOtherProfit(RoundUtil.convert(order.getOtherProfit() + profit, 2));
                        }
                    }
                    // count total order items and profit of month
                    order.setTotalOrder(order.getTotalOrder() + 1);
                    order.setTotalProfit(RoundUtil.convert(order.getTotalProfit() + profit, 2));

                    // add to list
                    orderEachMonths.set(month - 1, order);
                });
        return orderEachMonths;
    }

    public List<OrderHistoryResponse> getHistory(Long customerNumber) {
        return orderNoDSLRepository.findOrderByCustomerNumber(customerNumber).stream()
                .map(tuple -> OrderHistoryResponse.builder()
                        .orderNumber(tuple.get("orderNumber", Integer.class))
                        .orderDate(tuple.get("orderDate", Date.class))
                        .totalAmount(tuple.get("totalAmount", Double.class))
                        .status(tuple.get("status", String.class))
                        .build()
                )
                .toList();
    }
}
