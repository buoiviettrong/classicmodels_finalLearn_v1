package com.nixagh.classicmodels.repository.impl;

import com.nixagh.classicmodels.entity.Product;
import com.nixagh.classicmodels.repository.ProductRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.NumberExpression;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ProductRepositoryImpl extends BaseRepositoryImpl<Product, String> implements ProductRepository {
    public ProductRepositoryImpl(EntityManager entityManager) {
        super(Product.class, entityManager);
    }

    @Override
    public Product findProductByProductCode(String productCode) {
        return jpaQueryFactory
                .select(product)
                .from(product)
                .where(product.productCode.eq(productCode))
                .fetchFirst();

    }

    @Override
    public List<Tuple> findProductsEachMonthInYear_(int year) {
        // câu lệnh tính tổng profit của mỗi order detail
        NumberExpression<Double> profit = orderDetail.priceEach.multiply(orderDetail.quantityOrdered).sum();
        // câu lệnh lấy tháng của order
        NumberExpression<Integer> month = order.orderDate.month();

        return jpaQueryFactory
                .select(
                        month.as("month"),
                        product.productCode,
                        product.productName,
                        orderDetail.quantityOrdered.sum().as("totalSoldQuantity"),
                        profit.as("totalProfit")
                )
                .from(orderDetail)
                .join(orderDetail.order, order)
                // điều kiện năm và trạng thái order = Shipped
                .where(order.orderDate.year().eq(year).and(order.status.eq("Shipped")))
                // group by tháng, productCode, productName
                .groupBy(month, product.productCode, product.productName)
                .stream().toList();
    }

    @Override
    public List<Tuple> findAllStatistic() {
        // câu lệnh tính tổng profit của mỗi order detail
        NumberExpression<Double> profit = orderDetail.priceEach.multiply(orderDetail.quantityOrdered).sum();
        // câu lệnh lấy tháng của order
        NumberExpression<Integer> month = order.orderDate.month();

        return jpaQueryFactory
                .select(
                        month.as("month"),
                        product.productCode,
                        product.productName,
                        customer.customerNumber,
                        customer.customerName,
                        orderDetail.quantityOrdered.sum().as("totalSoldQuantity"),
                        profit.as("totalProfit")
                )
                .from(orderDetail)
                .join(orderDetail.order, order)
                .join(order.customer, customer)
                // điều kiện năm và trạng thái order = Shipped
                .where(order.status.eq("Shipped"))
                // group by tháng, productCode, productName và customerNumber, customerName
                .groupBy(month, product.productCode, product.productName, customer.customerNumber, customer.customerName)
                .stream().toList();
    }

}
