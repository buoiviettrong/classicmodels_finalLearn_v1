package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.OrderDetail;
import com.nixagh.classicmodels.entity.embedded.OrderDetailsEmbed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailsEmbed> {

  List<OrderDetail> getOrderDetail(Long orderNumber);

  void deleteByOrderNumber(Long orderNumber);
}
