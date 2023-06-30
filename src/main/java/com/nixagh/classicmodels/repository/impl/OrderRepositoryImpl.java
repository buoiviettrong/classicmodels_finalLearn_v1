package com.nixagh.classicmodels.repository.impl;

import com.nixagh.classicmodels.dto.orders.OrderFilter;
import com.nixagh.classicmodels.entity.Order;
import com.nixagh.classicmodels.repository.OrderRepository;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.aspectj.weaver.ast.Or;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class OrderRepositoryImpl extends BaseRepositoryImpl<Order, Long> implements OrderRepository {
    public OrderRepositoryImpl(EntityManager entityManager) {
        super(Order.class, entityManager);
    }

    @Override
    public List<Order> getOrderByCustomerNumber(Long customerNumber) {
        return jpaQueryFactory
                .selectFrom(order)
                .where(order.customer.customerNumber.eq(customerNumber))
                .fetch();
    }

    @Override
    public Order getOrderByOrderNumber(Long orderNumber) {
        return jpaQueryFactory
                .selectFrom(order)
                .where(order.orderNumber.eq(orderNumber))
                .fetchFirst();
    }

    @Override
    public List<Order> getOrderByFilters(OrderFilter filter, Long page, Long size) {
        return getLimit(filter, page, size);
    }

    @Override
    public List<Order> getOrders() {
        return jpaQueryFactory
                .select(order)
                .from(order)
                .join(order.customer, customer)
                .offset(0)
                .limit(10)
                .fetchJoin()
                .fetch();
    }

    @Override
    public Long getCount(OrderFilter filter) {
        JPAQuery<Long> select = jpaQueryFactory.select(order.count());
        return getPredicates(select, filter)
                .from(order)
                .fetchOne();
    }

    @Override
    public Optional<Order> findOrderByOrderNumber(Long orderNumber) {
        return jpaQueryFactory
                .selectFrom(order)
                .where(order.orderNumber.eq(orderNumber))
                .stream().findFirst();
    }

    @Override
    @Transactional
    public void deleteByOrderNumber(Long orderNumber) {
        long num = jpaQueryFactory
                .delete(order)
                .where(order.orderNumber.eq(orderNumber))
                .execute();
    }

    private <T> JPAQuery<T> getPredicates(JPAQuery<T> queryFactory, OrderFilter orderFilter) {

        if (orderFilter.getCustomerNumber() != null)
            queryFactory.where(order.customer.customerNumber.eq(orderFilter.getCustomerNumber()));

        if (orderFilter.getOrderNumber() != null)
            queryFactory.where(order.orderNumber.eq(orderFilter.getOrderNumber()));

        if (orderFilter.getStatus() != null)
            queryFactory.where(order.status.eq(orderFilter.getStatus().getShippingStatus()));

        if (orderFilter.getOrderDate() != null)
            switch (orderFilter.getOrderDateType()) {
                case EQ -> queryFactory.where(order.orderDate.eq(orderFilter.getOrderDate()));
                case GT -> queryFactory.where(order.orderDate.gt(orderFilter.getOrderDate()));
                case GTE -> queryFactory.where(order.orderDate.gt(orderFilter.getOrderDate())
                        .or(order.orderDate.eq(orderFilter.getOrderDate())));
                case LT -> queryFactory.where(order.orderDate.lt(orderFilter.getOrderDate()));
                case LTE -> queryFactory.where(order.orderDate.lt(orderFilter.getOrderDate())
                        .or(order.orderDate.eq(orderFilter.getOrderDate())));
            }

        if (orderFilter.getRequireDate() != null)
            switch (orderFilter.getRequireDateType()) {
                case EQ -> queryFactory.where(order.orderDate.eq(orderFilter.getRequireDate()));
                case GT -> queryFactory.where(order.orderDate.gt(orderFilter.getRequireDate()));
                case GTE -> queryFactory.where(order.orderDate.gt(orderFilter.getRequireDate())
                        .or(order.orderDate.eq(orderFilter.getRequireDate())));
                case LT -> queryFactory.where(order.orderDate.lt(orderFilter.getRequireDate()));
                case LTE -> queryFactory.where(order.orderDate.lt(orderFilter.getRequireDate())
                        .or(order.orderDate.eq(orderFilter.getRequireDate())));
            }

        if (orderFilter.getShippedDate() != null)
            switch (orderFilter.getShippedDateType()) {
                case EQ -> queryFactory.where(order.orderDate.eq(orderFilter.getShippedDate()));
                case GT -> queryFactory.where(order.orderDate.gt(orderFilter.getShippedDate()));
                case GTE -> queryFactory.where(order.orderDate.gt(orderFilter.getShippedDate())
                        .or(order.orderDate.eq(orderFilter.getShippedDate())));
                case LT -> queryFactory.where(order.orderDate.lt(orderFilter.getShippedDate()));
                case LTE -> queryFactory.where(order.orderDate.lt(orderFilter.getShippedDate())
                        .or(order.orderDate.eq(orderFilter.getShippedDate())));
            }
        return queryFactory;
    }

    private List<Order> getLimit(OrderFilter filter, Long page, Long size) {
        JPAQuery<Order> select = jpaQueryFactory.select(order);

        return getPredicates(select, filter)
                .from(order)
                .leftJoin(order.customer, customer).fetchJoin()
                .leftJoin(customer.salesRepEmployeeNumber, employee).fetchJoin()
                .leftJoin(employee.officeCode, office).fetchJoin()
                .offset((page - 1) * size)
                .limit(size)
                .stream().toList();
    }

    private List<Order> getLimit(
            OrderFilter filter,
            ConstructorExpression<Order> projections,
            Long page,
            Long size
    ) {
        JPAQuery<Order> select = jpaQueryFactory.select(projections);
        return getPredicates(select, filter)
                .from(order)
                .offset((page - 1) * size)
                .limit(size)
                .stream().toList();
    }

}
