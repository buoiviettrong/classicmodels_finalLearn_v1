package com.nixagh.classicmodels.repository.impl;

import com.nixagh.classicmodels.entity.OrderDetail;
import com.nixagh.classicmodels.entity.embedded.OrderDetailsEmbed;
import com.nixagh.classicmodels.repository.OrderDetailRepository;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class OrderDetailRepositoryImpl extends BaseRepositoryImpl<OrderDetail, OrderDetailsEmbed> implements OrderDetailRepository {
    public OrderDetailRepositoryImpl(EntityManager entityManager) {
        super(OrderDetail.class, entityManager);
    }

    @Override
    public List<OrderDetail> getOrderDetail(Long orderNumber) {
        return jpaQueryFactory
                .selectFrom(orderDetail)
                .where(orderDetail.id.orderNumber.eq(orderNumber))
                .fetch();
    }

    @Override
    @Transactional
    public void deleteByOrderNumber(Long orderNumber) {
        long num = jpaQueryFactory
                .delete(orderDetail)
                .where(orderDetail.order.orderNumber.eq(orderNumber))
                .execute();
        System.out.println(num);
    }
}
