package com.nixagh.classicmodels.repository.authRepo.impl;

import com.nixagh.classicmodels.entity.user.User;
import com.nixagh.classicmodels.repository.authRepo.UserRepository;
import com.nixagh.classicmodels.repository.impl.BaseRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class UserRepositoryImpl extends BaseRepositoryImpl<User, Long> implements UserRepository {
    public UserRepositoryImpl(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return jpaQueryFactory
                .selectFrom(user)
                .join(user.role, role).fetchJoin()
                .join(role.permissions, permission).fetchJoin()
                .leftJoin(user.tokens, token).fetchJoin()
                .where(user.email.equalsIgnoreCase(email))
                .stream().findFirst();
    }

    @Override
    public Optional<User> checkEmail(String email) {
        return jpaQueryFactory
                .selectFrom(user)
                .where(user.email.equalsIgnoreCase(email))
                .stream().findFirst();
    }

    @Override
    @Transactional
    public void updateRefreshToken(Long id, String refreshToken) {
        jpaQueryFactory.update(user)
                .set(user.refreshToken, refreshToken)
                .where(user.id.eq(id))
                .execute();
    }
}
