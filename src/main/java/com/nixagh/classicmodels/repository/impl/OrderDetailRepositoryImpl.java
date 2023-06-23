package com.nixagh.classicmodels.repository.impl;

import com.nixagh.classicmodels.entity.OrderDetail;
import com.nixagh.classicmodels.entity.embedded.OrderDetailsEmbed;
import com.nixagh.classicmodels.repository.OrderDetailRepository;
import jakarta.persistence.EntityManager;

public class OrderDetailRepositoryImpl extends BaseRepositoryImpl<OrderDetail, OrderDetailsEmbed> implements OrderDetailRepository {
  public OrderDetailRepositoryImpl(EntityManager entityManager) {
    super(OrderDetail.class, entityManager);
  }
}
