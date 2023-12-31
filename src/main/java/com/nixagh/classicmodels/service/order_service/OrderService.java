package com.nixagh.classicmodels.service.order_service;

import com.nixagh.classicmodels.dto._statistic.synthetic.details.Details;
import com.nixagh.classicmodels.dto._statistic.synthetic.overview.OverviewTop;
import com.nixagh.classicmodels.dto.orders.*;
import com.nixagh.classicmodels.dto.orders.admin.AdminOrderResponse;
import com.nixagh.classicmodels.dto.orders.admin.statictis.customer.orders.detail.CustomerOrderDetailResponse;
import com.nixagh.classicmodels.dto.orders.admin.statictis.order.OrderDetailResponse;
import com.nixagh.classicmodels.dto.orders.admin.statictis.order.OrderDetails;
import com.nixagh.classicmodels.dto.orders.manager.history.OrderHistoryResponse;
import com.nixagh.classicmodels.dto.page.PageRequestInfo;
import com.nixagh.classicmodels.dto.page.PageResponseInfo;
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
import com.nixagh.classicmodels.exception.exceptions.*;
import com.nixagh.classicmodels.repository.customer.CustomerRepository;
import com.nixagh.classicmodels.repository.order.OrderNoDSLRepository;
import com.nixagh.classicmodels.repository.order.OrderRepository;
import com.nixagh.classicmodels.repository.order_detail.OrderDetailRepository;
import com.nixagh.classicmodels.repository.product.ProductRepository;
import com.nixagh.classicmodels.utils.math.RoundUtil;
import com.nixagh.classicmodels.utils.page.PageUtil;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final OrderNoDSLRepository orderNoDSLRepository;

    @Override
    public List<Order> getOrderByCustomerNumber(Long customerNumber) {
        return orderRepository.getOrderByCustomerNumber(customerNumber);
    }

    @Override
    public List<Order> getOrders() {
        return orderRepository.getOrders();
    }

    @Override
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

    @Override
    @Transactional
    public Order saveOrder(OrderCreateRequest order) {
        Customer customer = customerRepository.findByCustomerNumber(order.getCustomerNumber());
        Double totalPrice = 0.0;
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
            totalPrice += productOrders.get(i).getQuantity() * product_.getMsrp();
        }

        // cập nhập số lượng sản phẩm trong kho product
        for (ProductOrder productOrder : productOrders) {
            Product product_ = productRepository.findProductByProductCode(productOrder.getProductCode());
            product_.setQuantityInStock((int) (product_.getQuantityInStock() - productOrder.getQuantity()));
            productRepository.save(product_);
        }

        // trừ tiền khách hàng
        if (customer.getCreditLimit() < totalPrice)
            throw new NotEnoughMoney("Customer %s not enough money".formatted(customer.getCustomerName()));
        customer.setCreditLimit(customer.getCreditLimit() - totalPrice);
        customerRepository.save(customer);
        return result;
    }

    @Override
    public List<ProductDTO> getOrderDetailByOrderNumber(Long orderNumber) {
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

    @Override
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

    @Override
    public long deleteOrder(Long orderNumber) {
        Order order = orderRepository.findOrderByOrderNumber(orderNumber).orElseThrow(() -> new NotFoundEntity("Order not found"));

        // delete order details
        orderDetailRepository.deleteByOrderNumber(orderNumber);
        // delete order
        orderRepository.deleteByOrderNumber(orderNumber);

        return order.getOrderNumber();
    }

    @Override
    public Order getOrderByOrderNumber(Long orderNumber) {
        return orderRepository.findOrderByOrderNumber(orderNumber).orElseThrow(() -> new NotFoundEntity("Order " + orderNumber + " not found"));
    }

    @Override
    public HighestOrderResponse getHighestOrder() {
        return orderRepository.getHighestOrder();
    }

    @Override
    public List<OrderWithProfit> getOrderByTimeRange(@DateTimeFormat(pattern = "yyyy-MM-dd") java.sql.Date from,
                                                     @DateTimeFormat(pattern = "yyyy-MM-dd") java.sql.Date to) {
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

    @Override
    public Long getTotalOrder(Date from, Date to) {
        return orderRepository.getTotalOrder(from, to);
    }

    @Override
    public OverviewTop.Invoice getTop1Order(Date from, Date to) {
        Tuple tuple = orderRepository.getTop1Order(from, to);
        if (tuple == null) return OverviewTop.Invoice.builder()
                .orderNumber(-1L)
                .orderDate(new Date())
                .totalMoney(0.0)
                .build();

        return OverviewTop.Invoice.builder()
                .orderNumber(tuple.get(0, Long.class) == null ? -1L : tuple.get(0, Long.class))
                .orderDate(tuple.get(1, Date.class) == null ? new Date(0) : tuple.get(1, Date.class))
                .totalMoney(tuple.get(2, Double.class) == null ? 0.0 : RoundUtil.convert(tuple.get(2, Double.class), 2))
                .build();
    }

    @Override
    public List<Details> getOrderByEachTime(Date from, Date to) {
        return orderRepository.getOrderByEachTime(from, to)
                .stream()
                .map(tuple -> Details.builder()
                        .date(tuple.get(0, Date.class))
                        .totalMoney(RoundUtil.convert(tuple.get(1, Double.class), 2))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public AdminOrderResponse getAdminOrder(String status, String paymentStatus, String fromDate, String toDate, Long pageNumber, Long pageSize) {
        AdminOrderResponse response = new AdminOrderResponse();

        if ("All".equalsIgnoreCase(status)) status = "";
        if ("All".equalsIgnoreCase(paymentStatus)) paymentStatus = "";
        if (pageNumber == null || pageNumber < 1) pageNumber = 1L;
        if (pageSize == null || pageSize < 1) pageSize = 10L;


        var offset = (pageNumber - 1) * pageSize;
        var from = fromDate == null ? null : java.sql.Date.valueOf(fromDate);
        var to = toDate == null ? null : java.sql.Date.valueOf(toDate);

        System.out.println("fromDate: " + fromDate);
        System.out.println("toDate: " + toDate);

        List<AdminOrderResponse.OrderResponse> orders = orderRepository.getOrders(status, paymentStatus, from, to, offset, pageSize)
                .stream()
                .map(tuple -> {
                    var orderNumber = tuple.get(0, Long.class);
                    var orderDate = tuple.get(1, Date.class);
                    var shipDate = tuple.get(2, Date.class);
                    var status_ = tuple.get(3, String.class);
                    var customerNumber = tuple.get(4, Long.class);
                    var comment = tuple.get(5, String.class);
                    var totalAmount = tuple.get(6, Double.class);
                    var paymentStatus_ = tuple.get(7, String.class) == null ? "UNPAID" : tuple.get(7, String.class);
                    var paymentDate = tuple.get(8, Date.class);

                    return new AdminOrderResponse.OrderResponse(orderNumber, orderDate, shipDate, status_, customerNumber, comment, totalAmount, paymentStatus_, paymentDate);
                })
                .toList();
        Long total = orderRepository.countOrders(status, paymentStatus, from, to);

        PageResponseInfo pageResponseInfo = PageUtil.getResponse(pageNumber, pageSize, total == null ? 0L : total, (long) orders.size());

        response.setOrders(orders);
        response.setPageResponseInfo(pageResponseInfo);
        return response;
    }

    @Override
    public Map<Integer, Double> getProfitEachMonthInYear(int year) {
        Map<Integer, Double> map = new HashMap<>();
        for (int i = 1; i <= 12; i++) map.put(i, 0.0);

        orderNoDSLRepository.getProfitEachMonthInYear(year)
                .forEach(tuple -> map.put(tuple.get("month", Integer.class), tuple.get("profit", Double.class)));
        return map;
    }

    @Override
    public OrderStatisticResponse getOrderStatistical(StatisticalRequest statisticalRequest) {
        long offset = (statisticalRequest.getPageInfo().getPageNumber() - 1) * statisticalRequest.getPageInfo().getPageSize();
        long limit = statisticalRequest.getPageInfo().getPageSize();

        List<OrderStatisticDTO> orders = orderRepository.getOrderStatistical(statisticalRequest.getFrom(), new java.sql.Date(statisticalRequest.getTo().getTime() + 86400000), offset, limit)
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public Map<String, String> changeStatus(Long orderNumber, String status) {
        orderNoDSLRepository.findByOrderNumber(orderNumber).orElseThrow(() -> new NotFoundEntity("Order not found"));

        Arrays.stream(ShippingStatus.values())
                .map(ShippingStatus::getShippingStatus)
                .filter(s -> s.equals(status))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Status not found"));

        orderRepository.updateStatus(orderNumber, status);
        return Map.of("message", "success");
    }

    @Override
    public List<CustomerOrderDetailResponse> getOrderDetailByCustomerNumber(Long customerNumber, Integer year, Integer month) {
        return orderNoDSLRepository.getCustomerOrderDetails(customerNumber, year, month)
                .stream()
                .map(tuple -> CustomerOrderDetailResponse.builder()
                        .orderNumber(tuple.get("orderNumber", Integer.class))
                        .orderDate(tuple.get("orderDate", Date.class))
                        .shippedDate(tuple.get("shippedDate", Date.class))
                        .status(tuple.get("status", String.class))
                        .comments(tuple.get("comments", String.class))
                        .totalPrice(tuple.get("totalPrice", Double.class))
                        .build())
                .toList();
    }

    @Override
    public OrderDetailResponse getOrderDetails(Integer year, Integer month, String status, Long pageNumber, Long pageSize) {
        if (status == null || status.equals("All")) status = null;
        // check status
        if (status != null) {
            String finalStatus = status;
            Arrays.stream(ShippingStatus.values())
                    .map(ShippingStatus::getShippingStatus)
                    .filter(s -> s.equals(finalStatus))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException("Status not found"));
        }

        Long offset = (pageNumber - 1) * pageSize;
        Long total = (long) orderRepository.countOrderDetails(year, month).size();

        List<OrderDetails> orderDetails = orderRepository.getOrderDetails(year, month, status, offset, pageSize)
                .stream()
                .map(tuple -> OrderDetails.builder()
                        .orderNumber(tuple.get(0, Long.class))
                        .customerName(tuple.get(1, String.class))
                        .customerNumber(tuple.get(2, Long.class))
                        .orderDate(tuple.get(3, Date.class))
                        .shippedDate(tuple.get(4, Date.class))
                        .status(tuple.get(5, String.class))
                        .comments(tuple.get(6, String.class))
                        .totalPrice(RoundUtil.convert(tuple.get(7, Double.class), 2))
                        .build())
                .toList();

        Map<String, Integer> statusMap = orderRepository.getStatusMap(year, month)
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, String.class),
                        tuple -> Objects.requireNonNull(tuple.get(1, Long.class)).intValue()
                ));

        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        orderDetailResponse.setOrders(orderDetails);
        orderDetailResponse.setStatus(statusMap);
        orderDetailResponse.setPageResponseInfo(PageUtil.getResponse(pageNumber, pageSize, total, (long) orderDetails.size()));

        return orderDetailResponse;
    }
}
