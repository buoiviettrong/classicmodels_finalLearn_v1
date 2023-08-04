package com.nixagh.classicmodels.repository.token;

import com.nixagh.classicmodels.entity.auth.Token;
import com.nixagh.classicmodels.entity.auth.User;
import com.nixagh.classicmodels.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends BaseRepository<Token, Long> {
    Optional<Token> findByToken_(String jwt);

    List<Token> findAllValidTokenByUser(User user);

    void revokeAllUserTokens(User user);


    Optional<Token> checkTokenExistWithIpNotEqual(User user, String ip, String device);

    Optional<Long> checkTokenExistWithIpNotEqualORDeviceNotEqual(User user, String ip, String device);
}
