package com.nixagh.classicmodels.repository.product;

import com.nixagh.classicmodels.controller.ProductController;
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

import java.util.Date;
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
    public Tuple getTotalSoldProductAndProfit(java.util.Date from, java.util.Date to) {
        return jpaQueryFactory
                .select(
                        orderDetail.quantityOrdered.sum().as("totalSoldProduct"),
                        orderDetail.priceEach.multiply(orderDetail.quantityOrdered).sum().as("totalProfit")
                )
                .from(order)
                .leftJoin(order.orderDetail, orderDetail)
                .where(order.orderDate.between(from, to).and(order.status.eq("Shipped")))
                .fetchFirst();
    }

    @Override
    public Tuple getTop1Product(java.util.Date from, java.util.Date to) {
        return jpaQueryFactory
                .select(
                        product.productCode,
                        product.productName,
                        orderDetail.quantityOrdered.sum().as("totalSoldProduct"),
                        orderDetail.quantityOrdered.multiply(orderDetail.priceEach).sum().as("totalProfit")

                )
                .from(order)
                .join(order.orderDetail, orderDetail)
                .where(order.orderDate.between(from, to).and(order.status.eq("Shipped")))
                .groupBy(product.productCode, product.productName)
                .orderBy(orderDetail.quantityOrdered.sum().desc())
                .fetchFirst();
    }

    @Override
    public Tuple getTop1ProductLine(java.util.Date from, java.util.Date to) {
        return jpaQueryFactory
                .select(
                        product.productLine.productLine.as("ProductLineCode"),
                        product.productLine.productLine.count().as("quantity")

                )
                .from(order)
                .join(order.orderDetail, orderDetail)
                .join(orderDetail.product, product)
                .where(order.orderDate.between(from, to).and(order.status.eq("Shipped")))
                .groupBy(product.productLine.productLine)
                .orderBy(product.productLine.productLine.count().desc())
                .fetchFirst();
    }

    @Override
    public Long getTotalProduct(Date from, Date to) {
        return 0L;
    }

    @Override
    public List<Tuple> getSyntheticProductLineDSL(java.util.Date from, java.util.Date to) {
        return jpaQueryFactory
                .select(
                        productLine.productLine,
                        orderDetail.quantityOrdered.sum().as("totalSoldProduct"),
                        orderDetail.priceEach.multiply(orderDetail.quantityOrdered).sum().as("totalMoney")
                )
                .from(productLine)
                .leftJoin(productLine.productsList, product)
                .leftJoin(product.orderDetail, orderDetail)
                .leftJoin(orderDetail.order, order)
                .where(order.orderDate.between(from, to).and(order.status.eq("Shipped")))
                .groupBy(productLine.productLine)
                .orderBy(productLine.productLine.asc())
                .fetch();
    }

    @Override
    public Tuple getTotalSoldProductAndProfit(java.util.Date from, java.util.Date to, String typeProductLine, String search) {
        return jpaQueryFactory
                .select(
                        product.productLine.productLine,
                        orderDetail.quantityOrdered.sum().as("totalSold"),
                        orderDetail.priceEach.multiply(orderDetail.quantityOrdered).sum().as("totalMoney")
                )
                .from(order)
                .join(order.orderDetail, orderDetail)
                .join(orderDetail.product, product)
                .where(
                        order.orderDate.between(from, to)
                                .and(order.status.eq("Shipped"))
                                .and(product.productLine.productLine.containsIgnoreCase(typeProductLine))
                                .and(product.productCode.containsIgnoreCase(search)
                                        .or(product.productName.containsIgnoreCase(search)))
                )
                .fetchFirst();
    }

    @Override
    public List<Tuple> getDetailStatisticDetail(Date from, Date to, String typeProductLine, String search, long offset, long pageSize) {
        JPAQuery<Tuple> query = jpaQueryFactory
                .select(
                        product.productCode,
                        product.productName,
                        product.productLine.productLine,
                        orderDetail.quantityOrdered.sum().as("totalSold"),
                        orderDetail.priceEach.multiply(orderDetail.quantityOrdered).sum().as("totalMoney")
                )
                .from(order)
                .join(order.orderDetail, orderDetail)
                .join(orderDetail.product, product)
                .where(
                        order.orderDate.between(from, to)
                                .and(order.status.eq("Shipped"))
                                .and(product.productLine.productLine.containsIgnoreCase(typeProductLine))
                                .and(product.productCode.containsIgnoreCase(search)
                                        .or(product.productName.containsIgnoreCase(search)))
                )
                .groupBy(product.productCode, product.productName, product.productLine.productLine)
                .orderBy(orderDetail.quantityOrdered.sum().desc())
                .offset(offset)
                .limit(pageSize);
        return query.fetch();
    }

    @Override
    public Long countDetailStatisticDetail(java.sql.Date sqlFrom, java.sql.Date sqlTo, String typeProductLine, String search) {
        JPAQuery<Long> query = jpaQueryFactory
                .select(
                        product.productCode.countDistinct()
                )
                .from(order)
                .join(order.orderDetail, orderDetail)
                .join(orderDetail.product, product)
                .where(
                        order.orderDate.between(sqlFrom, sqlTo)
                                .and(order.status.eq("Shipped"))
                                .and(product.productLine.productLine.containsIgnoreCase(typeProductLine))
                                .and(product.productCode.containsIgnoreCase(search)
                                        .or(product.productName.containsIgnoreCase(search)))
                );
        return query.fetchFirst();
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
