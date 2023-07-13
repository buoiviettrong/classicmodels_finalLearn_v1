package com.nixagh.classicmodels.repository.impl;

import com.nixagh.classicmodels.dto.product.search.ProductSearchResponseDTO;
import com.nixagh.classicmodels.dto.product.search.QuantityInStock;
import com.nixagh.classicmodels.entity.Product;
import com.nixagh.classicmodels.exception.BadRequestException;
import com.nixagh.classicmodels.repository.ProductRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Product> findByProductCode(String productCode) {
        return jpaQueryFactory
                .selectFrom(product)
                .where(product.productCode.eq(productCode))
                .stream().findFirst();
    }

    @Override
    public List<ProductSearchResponseDTO> filterProducts(
//            String productCode,
//            String productName,
            String productLine,
            Integer productScale,
            String productVendor,
//            String productDescription,
            QuantityInStock quantityInStock,
            Long offset,
            Long pageSize
    ) {

        JPAQuery<ProductSearchResponseDTO> query = jpaQueryFactory.select(
                Projections.constructor(
                        ProductSearchResponseDTO.class,
                        product.productCode,
                        product.productName,
                        product.productLine.productLine,
                        product.productScale,
                        product.productVendor,
                        product.productDescription,
                        product.quantityInStock,
                        product.buyPrice,
                        product.msrp
                )
        ).from(product);
        query = getfilter(
                query,
//                productCode,
//                productName,
                productLine,
                productScale,
                productVendor,
//                productDescription,
                quantityInStock
        );
        return query.offset(offset).limit(pageSize).fetch();
    }

    @Override
    public Long countFilterProducts(
//            String productCode,
//            String productName,
            String productLine,
            Integer productScale,
            String productVendor,
//            String productDescription,
            QuantityInStock quantityInStock) {
        JPAQuery<Long> query = jpaQueryFactory.select(
                product.countDistinct()
        ).from(product);

        query = getfilter(
                query,
//                productCode,
//                productName,
                productLine,
                productScale,
                productVendor,
//                productDescription,
                quantityInStock
        );
        return query.fetchFirst();
    }

    @Override
    public ProductSearchResponseDTO getProduct(String productCode) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        ProductSearchResponseDTO.class,
                        product.productCode,
                        product.productName,
                        product.productLine.productLine,
                        product.productScale,
                        product.productVendor,
                        product.productDescription,
                        product.quantityInStock,
                        product.buyPrice,
                        product.msrp
                ))
                .from(product)
                .where(product.productCode.eq(productCode))
                .fetchFirst();
    }

    @Override
    public List<ProductSearchResponseDTO> managerSearch(String search, Long offset, Long pageSize) {
        BooleanExpression searchExpression = null;

        if (search != null)
            searchExpression = product.productCode.containsIgnoreCase(search)
                    .or(product.productName.containsIgnoreCase(search))
                    .or(product.productLine.productLine.containsIgnoreCase(search));

        return jpaQueryFactory
                .select(Projections.constructor(
                        ProductSearchResponseDTO.class,
                        product.productCode,
                        product.productName,
                        product.productLine.productLine,
                        product.productScale,
                        product.productVendor,
                        product.productDescription,
                        product.quantityInStock,
                        product.buyPrice,
                        product.msrp
                ))
                .from(product)
                .where(searchExpression)
                .offset(offset)
                .limit(pageSize)
                .fetch();
    }

    @Override
    public Long countManagerSearch(String search) {
        BooleanExpression searchExpression = null;

        if (search != null)
            searchExpression = product.productCode.containsIgnoreCase(search)
                    .or(product.productName.containsIgnoreCase(search))
                    .or(product.productLine.productLine.containsIgnoreCase(search));

        return jpaQueryFactory
                .select(product.countDistinct())
                .from(product)
                .where(searchExpression)
                .fetchFirst();
    }

    private <T> JPAQuery<T> getfilter(
            JPAQuery<T> query,
//            String productCode,
//            String productName,
            String productLine,
            Integer productScale,
            String productVendor,
//            String productDescription,
            QuantityInStock quantityInStock) {
//        if (productCode != null && !productCode.isEmpty()) {
//            query.where(product.productCode.eq(productCode));
//        }
//        if (productName != null && !productName.isEmpty()) {
//            query.where(product.productName.eq(productName));
//        }

        BooleanExpression productLinePredicate = null;
        BooleanExpression productScalePredicate = null;
        BooleanExpression productVendorPredicate = null;
        BooleanExpression quantityInStockPredicate = null;
        BooleanExpression quantityInStockMin = null;
        BooleanExpression quantityInStockMax = null;

        if (productLine != null && !productLine.isEmpty() && !productLine.equals("All"))
            productLinePredicate = product.productLine.productLine.eq(productLine);
        if (productScale != null && productScale != 0)
            productScalePredicate = product.productScale.eq("1:" + productScale);
        if (productVendor != null && !productVendor.isEmpty() && !productVendor.equals("All"))
            productVendorPredicate = product.productVendor.eq(productVendor);

//        if (productDescription != null && !productDescription.isEmpty()) {
//            query.where(product.productDescription.eq(productDescription));
//        }
        if (quantityInStock != null) {
            // null check

            if (quantityInStock.min() > quantityInStock.max())
                throw new BadRequestException("Min quantity in stock must be less than max quantity in stock");
            if (quantityInStock.min() > 0) {
                quantityInStockMin = product.quantityInStock.goe(Math.toIntExact(quantityInStock.min()));
            }
            if (quantityInStock.max() > 0) {
                quantityInStockMax = product.quantityInStock.loe(Math.toIntExact(quantityInStock.max()));
            }
            quantityInStockPredicate = quantityInStockMin == null ? quantityInStockMax : quantityInStockMin.and(quantityInStockMax);
        }

        return query.where(
                productLinePredicate,
                productScalePredicate,
                productVendorPredicate,
                quantityInStockPredicate
        );
    }

}
