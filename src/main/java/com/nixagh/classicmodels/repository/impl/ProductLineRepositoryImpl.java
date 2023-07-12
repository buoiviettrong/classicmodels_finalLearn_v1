package com.nixagh.classicmodels.repository.impl;

import com.nixagh.classicmodels.entity.ProductLinee;
import com.nixagh.classicmodels.repository.ProductLineRepository;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ProductLineRepositoryImpl extends BaseRepositoryImpl<ProductLinee, String> implements ProductLineRepository {
    public ProductLineRepositoryImpl(EntityManager entityManager) {
        super(ProductLinee.class, entityManager);
    }

    @Override
    public List<ProductLinee> getProductLines() {
        return jpaQueryFactory
                .selectFrom(productLinee)
                .innerJoin(productLinee.productsList, product)
                .fetchJoin()
                .fetch();
    }

    @Override
    public ProductLinee getProductLine(String productLine_) {
        return jpaQueryFactory
                .selectFrom(productLinee)
                .join(productLinee.productsList, product)
                .where(productLinee.productLine.eq(productLine_))
                .fetchJoin()
                .fetchFirst();
    }

    @Override
    @Transactional
    public void deleteProductLinee(String productLine_) {
        jpaQueryFactory
                .delete(productLinee)
                .where(productLinee.productLine.eq(productLine_))
                .execute();
    }

    @Override
    public List<String> getProductLinesSelect() {
        return jpaQueryFactory
                .select(productLinee.productLine)
                .from(productLinee)
                .fetch();
    }
}
