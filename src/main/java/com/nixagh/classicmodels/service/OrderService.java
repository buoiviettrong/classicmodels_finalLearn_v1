package com.nixagh.classicmodels.service;

import com.nixagh.classicmodels.dto.PageRequestInfo;
import com.nixagh.classicmodels.dto.PageResponseInfo;
import com.nixagh.classicmodels.dto.ProductRepository;
import com.nixagh.classicmodels.dto.orders.*;
import com.nixagh.classicmodels.dto.product.ProductDTO;
import com.nixagh.classicmodels.dto.statistical.OrderWithProfit;
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
import com.nixagh.classicmodels.repository.CustomerRepository;
import com.nixagh.classicmodels.repository.OrderDetailRepository;
import com.nixagh.classicmodels.repository.OrderNoDSLRepository;
import com.nixagh.classicmodels.repository.OrderRepository;
import com.nixagh.classicmodels.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
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
}
