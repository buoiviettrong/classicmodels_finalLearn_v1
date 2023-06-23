package com.nixagh.classicmodels.service;

import com.nixagh.classicmodels._common.PageUtil;
import com.nixagh.classicmodels.dto.*;
import com.nixagh.classicmodels.dto.orders.*;
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

import java.util.Date;
import java.util.List;

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
    for(int i = 0; i < productOrders.size(); i++) {
      Product product_ = productRepository.findProductByProductCode(productOrders.get(i).getProductCode());

      OrderDetail orderDetail = OrderDetail.builder()
          .id(new OrderDetailsEmbed(result.getOrderNumber() ,product_.getProductCode()))
          .order(saveOrder)
          .product(product_)
          .quantityOrdered(productOrders.get(i).getQuantity())
          .priceEach(productOrders.get(i).getPrice())
          .orderLineNumber(i+1)
          .build();
      orderDetailRepository.save(orderDetail);
    };

    return result;
  }
}
