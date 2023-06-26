package com.nixagh.classicmodels.repository.impl;

import com.nixagh.classicmodels.entity.token.Token;
import com.nixagh.classicmodels.entity.user.User;
import com.nixagh.classicmodels.repository.TokenRepository;
import jakarta.persistence.EntityManager;

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
        .where(token.token_.eq(jwt))
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
}
