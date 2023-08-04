package com.nixagh.classicmodels.repository.token;

import com.nixagh.classicmodels.entity.auth.QToken;
import com.nixagh.classicmodels.entity.auth.Token;
import com.nixagh.classicmodels.entity.auth.User;
import com.nixagh.classicmodels.repository.BaseRepositoryImpl;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class TokenRepositoryImpl extends BaseRepositoryImpl<Token, Long> implements TokenRepository {
    public TokenRepositoryImpl(EntityManager entityManager) {
        super(Token.class, entityManager);
    }

    public List<Token> findAllByUserId(Long user_id) {
        return jpaQueryFactory
                .select(token)
                .from(token)
                .where(token.user.id.eq(user_id))
                .fetchJoin()
                .stream().toList();
    }

    @Override
    public Optional<Token> findByToken_(String jwt) {
        return jpaQueryFactory
                .selectFrom(token)
                .where(token.accessToken.eq(jwt))
                .stream().findFirst();
    }

    @Override
    public List<Token> findAllValidTokenByUser(User user) {
        return jpaQueryFactory
                .selectFrom(token)
                .where(
                        token.user.eq(user),
                        token.expired.isFalse(),
                        token.revoked.isFalse()
                )
                .stream().toList();
    }

    @Override
    @Transactional
    public void revokeAllUserTokens(User user) {
        jpaQueryFactory
                .update(token)
                .set(token.revoked, true)
                .set(token.expired, true)
                .where(token.user.eq(user))
                .execute();
    }

    @Override
    public Optional<Token> checkTokenExistWithIpNotEqual(User user, String ip, String device) {
        return jpaQueryFactory
                .select(token)
                .from(token)
                .where(
                        token.user.eq(user),
                        token.ip.ne(ip),
//                        token.device.eq(device),
                        token.revoked.isFalse(),
                        token.expired.isFalse()
                )
                .stream().findFirst();
    }

    @Override
    @Transactional
    @Modifying
    public long checkTokenExistWithIpNotEqualORDeviceNotEqual(User user, String ip, String device) {

        List<Long> subQuery = jpaQueryFactory
                .selectDistinct(token.id.as("id"))
                .from(token)
                .where(
                        token.user.eq(user),
                        (token.ip.ne(ip).or(token.device.ne(device))),
                        token.revoked.isFalse(),
                        token.expired.isFalse()
                ).stream().toList();

        JPAUpdateClause updateClause = jpaQueryFactory.update(token);

        updateClause.set(token.revoked, true)
                .set(token.expired, true)
                .where(token.id.in(subQuery));

        return updateClause.execute();
    }

}
