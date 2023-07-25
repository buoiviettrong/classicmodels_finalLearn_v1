package com.nixagh.classicmodels.repository.product_line;

import com.nixagh.classicmodels.entity.ProductLinee;
import com.nixagh.classicmodels.repository.BaseRepositoryImpl;
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
                .selectFrom(productLine)
                .innerJoin(productLine.productsList, product)
                .fetchJoin()
                .fetch();
    }

    @Override
    public ProductLinee getProductLine(String productLine_) {
        return jpaQueryFactory
                .selectFrom(productLine)
                .join(productLine.productsList, product)
                .where(productLine.productLine.eq(productLine_))
                .fetchJoin()
                .fetchFirst();
    }

    @Override
    @Transactional
    public void deleteProductLinee(String productLine_) {
        jpaQueryFactory
                .delete(productLine)
                .where(productLine.productLine.eq(productLine_))
                .execute();
    }

    @Override
    public List<String> getProductLinesSelect() {
        return jpaQueryFactory
                .select(productLine.productLine)
                .from(productLine)
                .fetch();
    }
}
