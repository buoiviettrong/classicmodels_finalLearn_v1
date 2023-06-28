package com.nixagh.classicmodels.service;

import com.nixagh.classicmodels._common.PageUtil;
import com.nixagh.classicmodels.dto.Message.Message;
import com.nixagh.classicmodels.dto.PageRequestInfo;
import com.nixagh.classicmodels.dto.PageResponseInfo;
import com.nixagh.classicmodels.dto.ProductRepository;
import com.nixagh.classicmodels.dto.orderDetail.OrderDetailByOrderNumber;
import com.nixagh.classicmodels.dto.orders.*;
import com.nixagh.classicmodels.dto.product.ProductDTO;
import com.nixagh.classicmodels.entity.Customer;
import com.nixagh.classicmodels.entity.Order;
import com.nixagh.classicmodels.entity.OrderDetail;
import com.nixagh.classicmodels.entity.Product;
import com.nixagh.classicmodels.entity.embedded.OrderDetailsEmbed;
import com.nixagh.classicmodels.entity.enums.ShippingStatus;
import com.nixagh.classicmodels.repository.CustomerRepository;
import com.nixagh.classicmodels.repository.OrderDetailRepository;
import com.nixagh.classicmodels.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final OrderDetailRepository orderDetailRepository;
  private final ProductRepository productRepository;

  public OrderService(OrderRepository orderRepository,
                      CustomerRepository customerRepository,
                      OrderDetailRepository orderDetailRepository,
                      ProductRepository productRepository) {
    this.orderRepository = orderRepository;
    this.customerRepository = customerRepository;
    this.orderDetailRepository = orderDetailRepository;
    this.productRepository = productRepository;
  }

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

    var result = orderRepository.save(saveOrder);
    List<ProductOrder> productOrders = order.getProducts();
    for (int i = 0; i < productOrders.size(); i++) {
      Product product_ = productRepository.findProductByProductCode(productOrders.get(i).getProductCode());

      OrderDetail orderDetail = OrderDetail.builder()
          .id(new OrderDetailsEmbed(result.getOrderNumber(), product_.getProductCode()))
          .order(saveOrder)
          .product(product_)
          .quantityOrdered(productOrders.get(i).getQuantity())
          .priceEach(productOrders.get(i).getPrice())
          .orderLineNumber(i + 1)
          .build();
      orderDetailRepository.save(orderDetail);
    }

    return result;
  }

  public OrderDetailByOrderNumber getOrderDetail(Long orderNumber) {
    List<OrderDetail> orderDetailList = orderDetailRepository.getOrderDetail(orderNumber);
    return new OrderDetailByOrderNumber(
        orderDetailList.get(0).getId(),
        orderDetailList.get(0).getOrder(),
        orderDetailList.stream()
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
            .collect(Collectors.toList())
    );
  }

  public OrderUpdateResponse updateOrder(Long orderNumber, OrderUpdateRequest request) {
    OrderUpdateResponse orderUpdateResponse = new OrderUpdateResponse();
    List<Message> messages = new ArrayList<Message>();
    Order order = orderRepository.findOrderByOrderNumber(orderNumber);

    if (order == null) {
      messages.add(new Message("ORDER_UPDATE", "Order not found"));
      orderUpdateResponse.setMessages(messages);
      return orderUpdateResponse;
    }

    if(request.getStatus() != null) {
      ShippingStatus status = request.getStatus();
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
        default -> {
          messages.add(new Message("ORDER_UPDATE", "Wrong order status"));
          orderUpdateResponse.setMessages(messages);
          return orderUpdateResponse;
        }
      }
    }

    if (request.getShippedDate() != null) order.setShippedDate(request.getShippedDate());

    if (request.getComment() != null) order.setComments(request.getComment());

    orderRepository.save(order);

    orderUpdateResponse.setOrder(order);
    return orderUpdateResponse;
  }

  public OrderDeleteResponse deleteOrder(Long orderNumber) {
    Order order = orderRepository.findOrderByOrderNumber(orderNumber);
    OrderDeleteResponse orderDeleteResponse = new OrderDeleteResponse();
    List<Message> messages = new ArrayList<Message>();

    if (order == null) {
      messages.add(new Message("ORDER_DELETE", "Order not found"));
      orderDeleteResponse.setMessages(messages);
      return orderDeleteResponse;
    }
    // delete order details
    orderDetailRepository.deleteByOrderNumber(orderNumber);
//    System.out.println();
    // delete order
    orderRepository.deleteByOrderNumber(orderNumber);

    orderDeleteResponse.setOrderId(order.getOrderNumber());
    return orderDeleteResponse;
  }

  public OrderGetResponse getOrder(Long orderNumber) {
    OrderGetResponse orderResponse = new OrderGetResponse();
    Order order = orderRepository.findOrderByOrderNumber(orderNumber);

    if (order == null) {
      List<Message> messages = new ArrayList<>();
      messages.add(new Message("ORDER_GET", "Order not found"));
      orderResponse.setMessages(messages);
      return orderResponse;
    }
    orderResponse.setOrder(order);
    return orderResponse;
  }
}
