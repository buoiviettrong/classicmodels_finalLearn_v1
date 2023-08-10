package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.*;
import com.nixagh.classicmodels.entity.auth.*;
import com.nixagh.classicmodels.entity.chat.QChatMessage;
import com.nixagh.classicmodels.entity.chat.QRoom;
import com.nixagh.classicmodels.entity.chat.QRoomMembers;
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
    protected final QProductLinee productLine = QProductLinee.productLinee;
    protected final QPayment payment = QPayment.payment;
    protected final QOrderDetail orderDetail = QOrderDetail.orderDetail;
    protected final QUser user = QUser.user;
    protected final QToken token = QToken.token;
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

    protected final QChatMessage chatMessage = QChatMessage.chatMessage;
    protected final QRoom room = QRoom.room;
    protected final QRoomMembers roomMembers = QRoomMembers.roomMembers;

}
