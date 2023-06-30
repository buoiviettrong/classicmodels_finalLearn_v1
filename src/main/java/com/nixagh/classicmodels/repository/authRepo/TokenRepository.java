package com.nixagh.classicmodels.repository.authRepo;

import com.nixagh.classicmodels.entity.token.Token;
import com.nixagh.classicmodels.entity.user.User;
import com.nixagh.classicmodels.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends BaseRepository<Token, Long> {
    Optional<Token> findByToken_(String jwt);

    List<Token> findAllValidTokenByUser(User user);

    void revokeAllUserTokens(User user);
}
