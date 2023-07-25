package com.nixagh.classicmodels.repository.product;

import com.nixagh.classicmodels.controller.ProductController;
import com.nixagh.classicmodels.dto._statistic.overview.OverviewTop;
import com.nixagh.classicmodels.dto.product.search.ProductSearchResponseDTO;
import com.nixagh.classicmodels.dto.product.search.QuantityInStock;
import com.nixagh.classicmodels.entity.Product;
import com.nixagh.classicmodels.exception.exceptions.BadRequestException;
import com.nixagh.classicmodels.repository.BaseRepositoryImpl;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;

import java.sql.Date;
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
            String search,
            String productLine,
            Integer productScale,
            String productVendor,
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
                search,
                productLine,
                productScale,
                productVendor,
                quantityInStock
        );
        return query.offset(offset).limit(pageSize).fetch();
    }

    @Override
    public Long countFilterProducts(
            String search,
            String productLine,
            Integer productScale,
            String productVendor,
            QuantityInStock quantityInStock) {
        JPAQuery<Long> query = jpaQueryFactory.select(
                product.countDistinct()
        ).from(product);

        query = getfilter(
                query,
                search,
                productLine,
                productScale,
                productVendor,
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
    public List<ProductSearchResponseDTO> managerSearch(String search, String productLine, Long offset, Long pageSize) {
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
                .where(managerSearchFilter(search, productLine))
                .offset(offset)
                .limit(pageSize)
                .fetch();
    }

    private Predicate[] managerSearchFilter(String search, String productLine) {
        BooleanExpression searchExpression = null;
        BooleanExpression productLineExpression = null;

        if (search != null)
            searchExpression = product.productCode.containsIgnoreCase(search)
                    .or(product.productName.containsIgnoreCase(search))
                    .or(product.productLine.productLine.containsIgnoreCase(search));

        if (productLine != null && !productLine.equalsIgnoreCase("all"))
            productLineExpression = product.productLine.productLine.eq(productLine);

        Predicate[] predicates = new Predicate[2];
        predicates[0] = searchExpression;
        predicates[1] = productLineExpression;
        return predicates;
    }

    @Override
    public Long countManagerSearch(String search, String productLine) {

        return jpaQueryFactory
                .select(product.countDistinct())
                .from(product)
                .where(managerSearchFilter(search, productLine))
                .fetchFirst();
    }

    @Override
    public List<ProductController.ProductOutOfStockResponse> getOutOfStockProducts() {
        int MINIMUM_QUANTITY = 10;

        return jpaQueryFactory
                .select(Projections.constructor(
                        ProductController.ProductOutOfStockResponse.class,
                        product.productCode,
                        product.productName,
                        product.quantityInStock
                ))
                .from(product)
                .where(product.quantityInStock.lt(MINIMUM_QUANTITY))
                .fetch();
    }

    @Override
    public Tuple getTotalSoldProductAndProfit(String from, String to) {
        return jpaQueryFactory
                .select(
                        orderDetail.quantityOrdered.sum().as("totalSoldProduct"),
                        orderDetail.quantityOrdered.multiply(orderDetail.priceEach).sum().as("totalProfit")
                )
                .leftJoin(order.orderDetail, orderDetail)
                .where(order.orderDate.between(Date.valueOf(from), Date.valueOf(to)))
                .groupBy(order.orderDate)
                .fetchFirst();
    }

    @Override
    public OverviewTop.Product getTop1Product(String from, String to) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                OverviewTop.Product.class,
                                product.productCode,
                                product.productName,
                                orderDetail.quantityOrdered.sum().as("totalSoldProduct"),
                                orderDetail.quantityOrdered.multiply(orderDetail.priceEach).sum().as("totalProfit")
                        )
                )
                .from(order)
                .join(order.orderDetail, orderDetail)
                .where(order.orderDate.between(Date.valueOf(from), Date.valueOf(to)))
                .orderBy(orderDetail.quantityOrdered.sum().as("totalSoldProduct").desc())
                .fetchFirst();
    }

    @Override
    public OverviewTop.ProductLine getTop1ProductLine(String from, String to) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                OverviewTop.ProductLine.class,
                                product.productLine.productLine.as("ProductLineCode"),
                                product.productLine.productLine.count().as("quantity")
                        )
                )
                .from(order)
                .join(order.orderDetail, orderDetail)
                .join(orderDetail.product, product)
                .where(order.orderDate.between(Date.valueOf(from), Date.valueOf(to)))
                .groupBy(product.productLine.productLine)
                .orderBy(product.productLine.productLine.count().as("quantity").desc())
                .fetchFirst();
    }

    private <T> JPAQuery<T> getfilter(
            JPAQuery<T> query,
            String search,
            String productLine,
            Integer productScale,
            String productVendor,
            QuantityInStock quantityInStock) {

        BooleanExpression productLinePredicate = null;
        BooleanExpression productScalePredicate = null;
        BooleanExpression productVendorPredicate = null;
        BooleanExpression quantityInStockPredicate = null;
        BooleanExpression quantityInStockMin = null;
        BooleanExpression quantityInStockMax = null;
        BooleanExpression searchExpression = null;

        if (productLine != null && !productLine.isEmpty() && !productLine.equals("All"))
            productLinePredicate = product.productLine.productLine.eq(productLine);
        if (productScale != null && productScale != 0)
            productScalePredicate = product.productScale.eq("1:" + productScale);
        if (productVendor != null && !productVendor.isEmpty() && !productVendor.equals("All"))
            productVendorPredicate = product.productVendor.eq(productVendor);
        if (search != null && !search.isEmpty())
            searchExpression = product.productCode.containsIgnoreCase(search)
                    .or(product.productName.containsIgnoreCase(search));

        System.out.println(searchExpression);

        if (quantityInStock != null) {
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
                searchExpression,
                productLinePredicate,
                productScalePredicate,
                productVendorPredicate,
                quantityInStockPredicate
        );
    }

}
