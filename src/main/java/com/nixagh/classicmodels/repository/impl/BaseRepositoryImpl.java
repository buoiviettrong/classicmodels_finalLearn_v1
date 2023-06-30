package com.nixagh.classicmodels.repository.impl;

import com.nixagh.classicmodels._common.settings.QAuthSettings;
import com.nixagh.classicmodels._common.settings.QOAuthClient;
import com.nixagh.classicmodels.entity.*;
import com.nixagh.classicmodels.entity.auth.QPermission;
import com.nixagh.classicmodels.entity.auth.QRole;
import com.nixagh.classicmodels.entity.token.QRefreshToken;
import com.nixagh.classicmodels.entity.token.QToken;
import com.nixagh.classicmodels.entity.user.QUser;
import com.nixagh.classicmodels.repository.BaseRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public abstract class BaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    protected final EntityManager entityManager;
    protected final JPAQueryFactory jpaQueryFactory;
    protected final QCustomer customer = QCustomer.customer;
    protected final QEmployee employee = QEmployee.employee;
    protected final QOffice office = QOffice.office;
    protected final QOrder order = QOrder.order;
    protected final QProduct product = QProduct.product;
    protected final QProductLinee productLinee = QProductLinee.productLinee;
    protected final QPayment payment = QPayment.payment;
    protected final QOrderDetail orderDetail = QOrderDetail.orderDetail;
    protected final QUser user = QUser.user;
    protected final QToken token = QToken.token;
    protected final QRefreshToken refreshToken = QRefreshToken.refreshToken;
    protected final QRole role = QRole.role;
    protected final QPermission permission = QPermission.permission;
    protected final QOAuthClient client = QOAuthClient.oAuthClient;
    protected final QAuthSettings settings = QAuthSettings.authSettings;

    public BaseRepositoryImpl(
            Class<T> domainClass,
            EntityManager entityManager
    ) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }
}
