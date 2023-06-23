package com.nixagh.classicmodels.repository;

import com.nixagh.classicmodels.entity.token.Token;
import com.nixagh.classicmodels.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends BaseRepository<Token, Long>{
    public Optional<Token> findByToken_(String jwt);

    public List<Token> findAllValidTokenByUser(User user);
}
