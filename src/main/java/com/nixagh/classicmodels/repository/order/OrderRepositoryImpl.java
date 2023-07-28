package com.nixagh.classicmodels.repository.order;

import com.nixagh.classicmodels.dto.orders.HighestOrderResponse;
import com.nixagh.classicmodels.dto.orders.OrderFilter;
import com.nixagh.classicmodels.entity.Order;
import com.nixagh.classicmodels.repository.BaseRepositoryImpl;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
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

    @Override
    public HighestOrderResponse getHighestOrder() {

        NumberExpression<?> orderNumber = order.orderNumber;
        NumberExpression<?> totalPrice = orderDetail.priceEach.sum();

        return jpaQueryFactory
                .select(Projections.constructor(
                        HighestOrderResponse.class,
                        orderNumber,
                        totalPrice
                ))
                .from(order)
                .leftJoin(order.orderDetail, orderDetail)
                .groupBy(orderNumber)
                .orderBy(totalPrice.desc())
                .fetchFirst();
    }

    @Override
    public List<Tuple> getOrderByTimeRange(java.sql.Date from, java.sql.Date to) {
        return jpaQueryFactory
                .select(
                        order.orderNumber,
                        order.orderDate,
                        order.requiredDate,
                        order.shippedDate,
                        order.status,
                        order.comments,
                        // sum of priceEach * quantityOrdered and round to 2 decimal places
                        orderDetail.priceEach.multiply(orderDetail.quantityOrdered).sum().round()
                )
                .from(order)
                .leftJoin(order.orderDetail, orderDetail)
                .where(order.orderDate.between(from, to).and(order.status.eq("Shipped")))
                .groupBy(order.orderNumber)
                .orderBy(order.orderDate.desc())
                .fetch();
    }

    @Override
    public List<Tuple> getOrderStatistical(Date from, Date to, long offset, long limit) {
        return jpaQueryFactory
                .select(
                        order.orderNumber,
                        order.orderDate,
                        order.shippedDate,
                        order.status,
                        order.customer.customerNumber,
                        order.comments
                )
                .from(order)
                .where(order.orderDate.between(from, to))
                .orderBy(order.orderDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Override
    public long countOrderStatistical(Date from, Date to) {
        return jpaQueryFactory
                .select(order.orderNumber.count())
                .from(order)
                .where(order.orderDate.between(from, to))
                .fetchFirst();
    }

    @Override
    @Transactional
    public void updateStatus(Long orderNumber, String status) {
        JPAUpdateClause update = jpaQueryFactory.update(order);
        if ("Shipped".equalsIgnoreCase(status)) {
            update.set(order.status, status)
                    .set(order.shippedDate, new java.util.Date())
                    .where(order.orderNumber.eq(orderNumber))
                    .execute();
        } else
            update.set(order.status, status)
                    .where(order.orderNumber.eq(orderNumber))
                    .execute();
    }

    @Override
    public List<Tuple> getOrderDetails(Integer year, Integer month, String status, Long offset, Long limit) {
        BooleanExpression statusExpression = null;
        if (status != null) statusExpression = order.status.eq(status);

        return jpaQueryFactory
                .select(
                        order.orderNumber,
                        customer.customerName,
                        customer.customerNumber,
                        order.orderDate,
                        order.shippedDate,
                        order.status,
                        order.comments,
                        orderDetail.priceEach.multiply(orderDetail.quantityOrdered).sum()
                )
                .from(order)
                .leftJoin(order.orderDetail, orderDetail)
                .leftJoin(order.customer, customer)
                .where(order.orderDate.year().eq(year).and(order.orderDate.month().eq(month)).and(statusExpression))
                .groupBy(
                        order.orderNumber,
                        customer.customerName,
                        customer.customerNumber,
                        order.orderDate,
                        order.shippedDate,
                        order.status,
                        order.comments
                )
                .orderBy(order.orderDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Override
    public List<Tuple> getStatusMap(Integer year, Integer month) {
        return jpaQueryFactory
                .select(
                        order.status,
                        order.status.count()
                )
                .from(order)
                .where(order.orderDate.year().eq(year).and(order.orderDate.month().eq(month)))
                .groupBy(order.status)
                .fetch();
    }

    @Override
    public List<Tuple> countOrderDetails(Integer year, Integer month) {
        return jpaQueryFactory
                .select(
                        order.orderNumber,
                        customer.customerNumber,
                        customer.customerName,
                        order.orderDate,
                        order.shippedDate,
                        order.status,
                        orderDetail.quantityOrdered.multiply(orderDetail.priceEach).sum(),
                        order.comments
                )
                .from(order)
                .leftJoin(order.orderDetail, orderDetail)
                .leftJoin(order.customer, customer)
                .where(order.orderDate.year().eq(year).and(order.orderDate.month().eq(month)))
                .groupBy(
                        order.orderNumber,
                        customer.customerName,
                        customer.customerNumber,
                        order.orderDate,
                        order.shippedDate,
                        order.status,
                        order.comments
                )
                .orderBy(order.orderDate.desc())
                .fetch();
    }

    @Override
    public Long getTotalOrder(java.util.Date from, java.util.Date to) {
        return jpaQueryFactory
                .select(order.orderNumber.count())
                .from(order)
                .where(order.orderDate.between(from, to))
                .fetchFirst();
    }

    @Override
    public Tuple getTop1Order(java.util.Date from, java.util.Date to) {
        return jpaQueryFactory
                .select(
                        order.orderNumber,
                        order.orderDate,
                        orderDetail.priceEach.multiply(orderDetail.quantityOrdered).sum()
                )
                .from(order)
                .join(order.orderDetail, orderDetail)
                .where(order.orderDate.between(from, to))
                .groupBy(order.orderNumber, order.orderDate)
                .orderBy(orderDetail.priceEach.multiply(orderDetail.quantityOrdered).sum().desc())
                .fetchFirst();
    }

    @Override
    public List<Tuple> getOrderByEachTime(java.util.Date from, java.util.Date to) {
        return jpaQueryFactory
                .select(
                        order.orderDate,
                        orderDetail.priceEach.multiply(orderDetail.quantityOrdered).sum().as("total")
                )
                .from(order)
                .join(order.orderDetail, orderDetail)
                .where(order.orderDate.between(from, to))
                .groupBy(order.orderDate)
                .orderBy(order.orderDate.asc())
                .fetch();
    }

    @Override
    public List<Tuple> getOrders(String status, String paymentStatus, Date fromDate, Date toDate, long offset, Long pageSize) {
        return jpaQueryFactory
                .select(
                        order.orderNumber,
                        order.orderDate,
                        order.shippedDate,
                        order.status,
                        order.customer.customerNumber,
                        order.comments,
                        orderDetail.priceEach.multiply(orderDetail.quantityOrdered).sum(),
                        order.paymentStatus,
                        order.paymentDate
                )
                .from(order)
                .leftJoin(order.orderDetail, orderDetail)
                .where(order.orderDate.between(fromDate, toDate)
                        .and(order.status.containsIgnoreCase(status))
                        .and(order.paymentStatus.like(paymentStatus + "%"))
                )
                .groupBy(order.orderNumber)
                .orderBy(order.orderDate.desc())
                .offset(offset)
                .limit(pageSize)
                .fetch();
    }

    @Override
    public Long countOrders(String status, String paymentStatus, Date fromDate, Date toDate) {
        return jpaQueryFactory
                .select(order.orderNumber.count())
                .from(order)
                .where(order.orderDate.between(fromDate, toDate)
                        .and(order.status.containsIgnoreCase(status))
                        .and(order.paymentStatus.like(paymentStatus + "%"))
                )
                .orderBy(order.orderDate.desc())
                .fetchFirst();
    }


    private <T> JPAQuery<T> getPredicates(JPAQuery<T> queryFactory, OrderFilter orderFilter) {
        if (orderFilter == null)
            return queryFactory;
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
