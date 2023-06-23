package com.nixagh.classicmodels.repository.impl;

import com.nixagh.classicmodels.entity.user.User;
import com.nixagh.classicmodels.repository.UserRepository;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class UserRepositoryImpl extends BaseRepositoryImpl<User, Long> implements UserRepository {
    public UserRepositoryImpl(EntityManager entityManager) {
        super(User.class, entityManager);
    }
    @Override
    public Optional<User> getUserByEmail(String email) {
        return jpaQueryFactory
                .selectFrom(user)
                .where(user.email.equalsIgnoreCase(email))
                .stream().findFirst();
    }
}
